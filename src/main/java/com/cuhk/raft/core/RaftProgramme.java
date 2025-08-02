package com.cuhk.raft.core;

import com.cuhk.raft.bean.ReplicationConfig;
import com.cuhk.raft.bean.ReplicatorBean;
import com.cuhk.raft.pb.Raft;
import com.cuhk.raft.utils.SignalUtils;
import com.cuhk.raft.utils.SingleThreadExecutorManager;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

public class RaftProgramme {

    Logger logger = Logger.getLogger(RaftProgramme.class);

    private final ReplicationConfig replicationConfig;
    private final RaftServerInr raftServerInr;
    private final int nodeId;
    private final int port;
    private final RaftCore raftCore;

    private List<RaftClientInr> raftClientInrList;

    public RaftProgramme(ReplicationConfig replicationConfig) {
        this.replicationConfig = replicationConfig;
        this.nodeId = replicationConfig.getNodeId();
        this.port = Integer.parseInt(replicationConfig.getReplicatorMap().get(this.nodeId).getAddress().split(":")[1]);
        this.raftServerInr = new RaftServerInr(port);
        this.raftCore = new RaftCore(this.nodeId, 1000, 5000);

    }

    public void run() throws InterruptedException {
        logger.info("Node "+ nodeId +" main programme starts...");

        //TODO 1.启动inner server线程
        new SingleThreadExecutorManager(()->raftServerStart()).start();

        //TODO 2.初始化inner clients
        int replications = replicationConfig.getReplications();
        raftClientInrList = new ArrayList<>(replications);
        for (Map.Entry<Integer, ReplicatorBean> entry : replicationConfig.getReplicatorMap().entrySet()) {
            Integer key = entry.getKey();
            //自己不需要client
            if (key == nodeId) continue;
            ReplicatorBean replicatorBean = entry.getValue();
            logger.info("Node "+ nodeId +" create client from " + replicatorBean);
            raftClientInrList.add(new RaftClientInr(replicatorBean));
        }

        //TODO 3.开始运行
        while (true) {
            //TODO 三个角色状态相互切换
            switch (this.raftCore.getCurrentRole()){
                //每个节点初始化都是follower
                case Follower:

                    logger.info("Node " + nodeId + " becomes Follower at Term " + raftCore.getCurrentTerm());
                    //等待leader的心跳或者其他有效candidate的要票,未收到变成candidate
                    Integer heartSignal = raftCore.electionRestQueue.poll(raftCore.getElectionTimeout(), TimeUnit.MILLISECONDS);
                    if (heartSignal == null)
                        this.raftCore.setCurrentRole(Raft.Role.Candidate);
                    break;

                case Candidate:
                    //1.自我任期+1
                    raftCore.termIncrement();
                    logger.info("Node " + nodeId + " becomes Candidate at term " + raftCore.getCurrentTerm());
                    //2.投票给自己
                    raftCore.setVotedFor(nodeId);
                    //3.广播要票
                    AtomicReference<Integer> voteNum = new AtomicReference<>(0);
                    for (RaftClientInr raftClientInr : raftClientInrList) {
                        //多线程同时发送
                        new Thread(() -> {
                            int requestVoteTerm = raftCore.getCurrentTerm();
                            Raft.RequestVoteReply reply = raftClientInr.requestVote(raftCore);
                            //得到票数，并且自己还是当前term的candidate
                            if (reply != null && reply.getVoteGranted() && requestVoteTerm == raftCore.getCurrentTerm()) {
                                logger.info("Node " + nodeId + " granted from Node id " + reply.getFrom() + " at term " + raftCore.getCurrentTerm());
                                voteNum.getAndSet(voteNum.get() + 1);
                                if(voteNum.get() == replications/2 && raftCore.getCurrentRole() == Raft.Role.Candidate) {
                                    //成为leader
                                    raftCore.setCurrentRole(Raft.Role.Leader);
                                    try {
                                        raftCore.electionRestQueue.put(SignalUtils.ELECTION_RESET_2_LEADER);
                                    } catch (InterruptedException e) {
                                        throw new RuntimeException(e);
                                    }
                                }
                            }
                        }).start();
                    }
                    //4.如果超时将重新发送请求
                    Integer electionSignal = raftCore.electionRestQueue.poll(raftCore.getElectionTimeout(), TimeUnit.MILLISECONDS);

                    if (electionSignal != null){
                        if (electionSignal == SignalUtils.ELECTION_RESET_2_LEADER){
                            //成为leader
                            break;
                        } else if (electionSignal == SignalUtils.ELECTION_RESET_2_FOLLOWER){
                            //在选举的时候，有更高的term的node发送requestVote请求，自动放弃candidate
                            raftCore.setCurrentRole(Raft.Role.Follower);
                        }
                    }else{
                        //说明此次选举已经超时,重制超时时间
                        raftCore.resetRandomElectionTimeout();
                    }
                    break;

                case Leader:
                    logger.info("Node " + nodeId + " becomes Leader at Term " + raftCore.getCurrentTerm());
                    //1. 成为leader立即向所有节点发出心跳
                    logger.info("Node " + nodeId + " firstly broadcast heartbeat at Term " + raftCore.getCurrentTerm());
                    for (RaftClientInr raftClientInr : raftClientInrList) {
                        new Thread(() -> {
                            Raft.AppendEntriesReply appendEntriesReply = raftClientInr.heatBeat(raftCore);
                        }).start();
                    }
                    //2. leader开始
                    while (raftCore.getCurrentRole() == Raft.Role.Leader) {
                        Integer signal = raftCore.heartIntervalRestQueue.poll(raftCore.getHeartBeatInterval(), TimeUnit.MILLISECONDS);
                        if (signal != null){
                            continue;
                        }
                        logger.info("Node " + nodeId + " normally broadcast heartbeat at term " + raftCore.getCurrentTerm());
                        for (RaftClientInr raftClientInr : raftClientInrList) {
                            new Thread(() -> {
                                Raft.AppendEntriesReply appendEntriesReply = raftClientInr.heatBeat(raftCore);
                            }).start();
                        }
                    }
                    break;
            }
        }


    }

    public void raftServerStart()  {
        this.raftServerInr.build(this.raftCore);
        try {
            this.raftServerInr.start();
            this.raftServerInr.blockUntilShutdown();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }


    }
}

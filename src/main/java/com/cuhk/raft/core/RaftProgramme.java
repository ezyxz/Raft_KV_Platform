package com.cuhk.raft.core;

import com.cuhk.raft.bean.ReplicationConfig;
import com.cuhk.raft.bean.ReplicatorBean;
import com.cuhk.raft.pb.Raft;
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
    private final RaftServerInr raftServer;
    private final int nodeId;
    private final int port;
    private final RaftCore raftCore;


    private List<RaftClientInr> raftClientInrList;

    public RaftProgramme(ReplicationConfig replicationConfig) {
        this.replicationConfig = replicationConfig;
        this.nodeId = replicationConfig.getNodeId();
        this.port = Integer.parseInt(replicationConfig.getReplicatorMap().get(this.nodeId).getAddress().split(":")[1]);
        this.raftServer = new RaftServerInr(port);
        this.raftCore = new RaftCore(this.nodeId, 1000, 1000);

    }

    public void run() throws InterruptedException {

        SingleThreadExecutorManager serverThread = new SingleThreadExecutorManager(
                ()->serverStart()
        );
        serverThread.start();

        int replications = replicationConfig.getReplications();
        raftClientInrList = new ArrayList<>(replications);
        Set<Map.Entry<Integer, ReplicatorBean>> entries = replicationConfig.getReplicatorMap().entrySet();
        for (Map.Entry<Integer, ReplicatorBean> entry : entries) {
            Integer key = entry.getKey();
            //自己不需要client
            if (key == nodeId) continue;
            ReplicatorBean replicatorBean = entry.getValue();
            raftClientInrList.add(new RaftClientInr(replicatorBean));
        }
        //每个节点初始化状态就是follower
        while (true) {
            switch (this.raftCore.getCurrentRole()){

                case Follower:
                    logger.info("Node " + nodeId + " becomes Follower at Term " + raftCore.getCurrentTerm());
                    //等待leader心跳,收到心跳重制,未收到变成candidate
                    Integer heartSignal = raftCore.heartBeatRestQueue.poll(raftCore.getHeartBeatInterval(), TimeUnit.MILLISECONDS);
                    if (heartSignal == null)
                        this.raftCore.setCurrentRole(Raft.Role.Candidate);
                    break;

                case Candidate:
                    //1.自我任期+1
                    raftCore.termIncrement();
                    logger.info("Node " + nodeId + " becomes Candidate at Term " + raftCore.getCurrentTerm());
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
                                logger.info("Node " + nodeId + " granted from Node id " + reply.getFrom());
                                voteNum.getAndSet(voteNum.get() + 1);
                                if(voteNum.get() == replications/2 && raftCore.getCurrentRole() == Raft.Role.Candidate) {
                                    //成为leader
                                    raftCore.setCurrentRole(Raft.Role.Leader);
                                    try {
                                        raftCore.electionRestQueue.put(1);
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
                        if (electionSignal == 1){
                            //成为leader
                            break;
                        } else if (electionSignal == 2){
                            //在此期间已经有别的node成为leader来
                            raftCore.setCurrentRole(Raft.Role.Follower);
                        }
                    }else{
                        //说明此次选举已经超时,重制超时时间
                        raftCore.resetElectionTimeout();
                    }


                    break;
                case Leader:
                    logger.info("Node " + nodeId + " becomes Leader at Term " + raftCore.getCurrentTerm());

                    break;
            }
        }


    }

    public void serverStart()  {
        this.raftServer.build(this.raftCore);
        try {
            this.raftServer.start();
            this.raftServer.blockUntilShutdown();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }


    }
}

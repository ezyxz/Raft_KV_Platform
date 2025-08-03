package com.cuhk.raft.core;

import com.cuhk.raft.bean.RaftStateBean;
import com.cuhk.raft.pb.Raft;
import com.cuhk.raft.pb.RaftNodeGrpc;
import com.cuhk.raft.utils.SignalUtils;
import io.grpc.stub.StreamObserver;
import lombok.Getter;
import lombok.Setter;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingDeque;

@Getter
public class RaftCore extends RaftNodeGrpc.RaftNodeImplBase{

    Logger logger = Logger.getLogger(RaftCore.class);

    //表示该节点的id
    private final int nodeId;

    //节点状态
    private RaftStateBean raftStateBean;

    //计时辅助
    public BlockingQueue<Integer> heartIntervalRestQueue =new LinkedBlockingDeque<>();
    public BlockingQueue<Integer> electionRestQueue =new LinkedBlockingDeque<>();

    //锁
    private Object voteLock = new Object();
    private Object appendEntryLock = new Object();


    public RaftCore(int nodeId, int heartBeatInterval, int electionTimeout) {
        this.nodeId = nodeId;
        this.raftStateBean = new RaftStateBean(nodeId, Raft.Role.Follower ,heartBeatInterval, electionTimeout);
    }

    public void termIncrement() {
        this.raftStateBean.setCurrentTerm(this.raftStateBean.getCurrentTerm() + 1);
    }

    public void resetRandomElectionTimeout(){
        this.raftStateBean.setElectionTimeout( 5000 + (int)(Math.random()*1000) );
    }

    @Override
    public void propose(Raft.ProposeArgs request, StreamObserver<Raft.ProposeReply> responseObserver) {
        super.propose(request, responseObserver);
    }

    @Override
    public void getValue(Raft.GetValueArgs request, StreamObserver<Raft.GetValueReply> responseObserver) {
        super.getValue(request, responseObserver);
    }

    @Override
    public void setElectionTimeout(Raft.SetElectionTimeoutArgs request, StreamObserver<Raft.SetElectionTimeoutReply> responseObserver) {
        super.setElectionTimeout(request, responseObserver);
    }

    @Override
    public void setHeartBeatInterval(Raft.SetHeartBeatIntervalArgs request, StreamObserver<Raft.SetHeartBeatIntervalReply> responseObserver) {
        super.setHeartBeatInterval(request, responseObserver);
    }

    @Override
    public void requestVote(Raft.RequestVoteArgs request, StreamObserver<Raft.RequestVoteReply> responseObserver) {
        //确保同一时间只能处理一个要票请求，防止同一term多次投票
        synchronized (voteLock) {
            logger.info("Node "+this.nodeId + " at term " + this.raftStateBean.getCurrentTerm() + " recv request vote from Node "+request.getFrom() + " whose term " + request.getTerm());

            int term = request.getTerm();
            int from = request.getFrom();

            boolean voteGranted = false;
            // Each server will vote for at most one candidate in a given term
            if (term > this.raftStateBean.getCurrentTerm() && this.raftStateBean.getTermVoteMap().get(term) == null) {
                logger.info("Node "+this.nodeId + " at term " + this.raftStateBean.getCurrentTerm() + " do vote from Node "+request.getFrom() + " whose term " + request.getTerm());
                this.raftStateBean.setVotedFor(from);
                voteGranted = true;
                this.raftStateBean.setCurrentRole(Raft.Role.Follower);
                this.raftStateBean.getTermVoteMap().put(term,from);
                try {
                    this.electionRestQueue.put(SignalUtils.ELECTION_RESET_2_FOLLOWER);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            Raft.RequestVoteReply requestVoteReply = Raft.RequestVoteReply.newBuilder()
                    .setTo(from)
                    .setFrom(this.nodeId)
                    .setTerm(this.raftStateBean.getCurrentTerm())
                    .setVoteGranted(voteGranted)
                    .build();
            responseObserver.onNext(requestVoteReply);
            responseObserver.onCompleted();
        }
    }

    @Override
    public void appendEntries(Raft.AppendEntriesArgs request, StreamObserver<Raft.AppendEntriesReply> responseObserver) {
        synchronized (appendEntryLock) {
            logger.info("Node "+this.nodeId + " at term " + this.raftStateBean.getCurrentTerm() + " recv appendEntries from Node "+request.getFrom() + " whose term " + request.getTerm());

            boolean rSuccess = false;
            int rterm = this.raftStateBean.getCurrentTerm();
            int from = request.getFrom();
            int leaderId = request.getLeaderId();
            int term = request.getTerm();
            int to = request.getTo();
            //无效appendEntries
            if ( term < this.raftStateBean.getCurrentTerm()){
                Raft.AppendEntriesReply appendEntriesReply = Raft.AppendEntriesReply.newBuilder()
                        .setFrom(nodeId)
                        .setTerm(this.raftStateBean.getCurrentTerm())
                        .setSuccess(false).build();
                responseObserver.onNext(appendEntriesReply);
                responseObserver.onCompleted();
                return;
            }
            this.raftStateBean.setVotedFor(from);
            if (term > this.raftStateBean.getCurrentTerm()){
                this.raftStateBean.setCurrentTerm(term);
                rterm = term;
            }

            try {
                this.electionRestQueue.put(SignalUtils.HEART_BEAT_RESET_2_FOLLOWER);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Raft.AppendEntriesReply appendEntriesReply = Raft.AppendEntriesReply.newBuilder()
                    .setSuccess(rSuccess)
                    .setTerm(this.raftStateBean.getCurrentTerm())
                    .setFrom(this.nodeId)
                    .build();
            responseObserver.onNext(appendEntriesReply);
            responseObserver.onCompleted();
        }
    }

    @Override
    public void checkEvents(Raft.CheckEventsArgs request, StreamObserver<Raft.CheckEventsReply> responseObserver) {
        super.checkEvents(request, responseObserver);
    }

    @Override
    public void whoAreYou(Raft.WhoAreYouArgs request, StreamObserver<Raft.WhoAreYouReply> responseObserver) {
        super.whoAreYou(request, responseObserver);
    }

}

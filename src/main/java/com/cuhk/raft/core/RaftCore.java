package com.cuhk.raft.core;

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


    private final int nodeId; //表示该节点的id
    private  int heartBeatInterval;  //作为leader时，心跳间隔时间 单位ms
    private  int electionTimeout; //作为candidate时，选举超市时间 单位ms
    private Map<Integer, Integer> termVoteMap = new ConcurrentHashMap<>();


    private int currentTerm = 0;
    @Setter
    private int votedFor = -1;
    @Setter
    private Raft.Role currentRole;
    @Getter
    int commitIndex = -1;

    public BlockingQueue<Integer> heartBeatRestQueue =new LinkedBlockingDeque<>();
    public BlockingQueue<Integer> heartIntervalRestQueue =new LinkedBlockingDeque<>();

    public BlockingQueue<Integer> electionRestQueue =new LinkedBlockingDeque<>();


    public RaftCore(int nodeId, int heartBeatInterval, int electionTimeout) {
        this.nodeId = nodeId;
        this.heartBeatInterval = heartBeatInterval;
        this.electionTimeout = electionTimeout;
        this.currentRole = Raft.Role.Follower;
    }

    public void termIncrement() {
        this.currentTerm++;
    }

    public void resetRandomElectionTimeout(){
        this.electionTimeout =  5000 + (int)(Math.random()*1000);
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
        logger.info("Node "+this.nodeId + " at term " + this.currentTerm + " recv request vote from Node "+request.getFrom() + "whose term " + request.getTerm());

        int term = request.getTerm();
        int from = request.getFrom();
        boolean voteGranted = false;
        //&&  Each server will vote for at most one candidate in a given term
        if (term > this.currentTerm && termVoteMap.get(term) == null) {
            logger.info("Node "+this.nodeId + " at term " + this.currentTerm + " do vote from Node "+request.getFrom() + " whose term " + request.getTerm());
            votedFor = from;
            voteGranted = true;
            this.currentRole = Raft.Role.Follower;
            termVoteMap.put(term,from);
            try {
                this.electionRestQueue.put(SignalUtils.ELECTION_RESET_2_FOLLOWER);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        Raft.RequestVoteReply requestVoteReply = Raft.RequestVoteReply.newBuilder()
                .setTo(from)
                .setFrom(this.nodeId)
                .setTerm(this.currentTerm)
                .setVoteGranted(voteGranted)
                .build();
        responseObserver.onNext(requestVoteReply);
        responseObserver.onCompleted();
    }

    @Override
    public void appendEntries(Raft.AppendEntriesArgs request, StreamObserver<Raft.AppendEntriesReply> responseObserver) {
        logger.info("Node "+this.nodeId + " at term " + this.currentTerm + " recv appendEntries from Node "+request.getFrom() + " whose term " + request.getTerm());

        boolean rSuccess = false;
        int rterm = this.currentTerm;
        int from = request.getFrom();
        int leaderId = request.getLeaderId();
        int term = request.getTerm();
        int to = request.getTo();
        //无效appendEntries
        if ( term < this.currentTerm){
            Raft.AppendEntriesReply appendEntriesReply = Raft.AppendEntriesReply.newBuilder()
                    .setFrom(nodeId)
                    .setTerm(this.currentTerm)
                    .setSuccess(false).build();
            responseObserver.onNext(appendEntriesReply);
            responseObserver.onCompleted();
            return;
        }
        votedFor = from;
        if (term > this.currentTerm){
            this.currentTerm = term;
            rterm = term;
        }

        try {
            this.electionRestQueue.put(SignalUtils.HEART_BEAT_RESET_2_FOLLOWER);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Raft.AppendEntriesReply appendEntriesReply = Raft.AppendEntriesReply.newBuilder()
                .setSuccess(rSuccess)
                .setTerm(this.currentTerm)
                .setFrom(this.nodeId)
                .build();
        responseObserver.onNext(appendEntriesReply);
        responseObserver.onCompleted();

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

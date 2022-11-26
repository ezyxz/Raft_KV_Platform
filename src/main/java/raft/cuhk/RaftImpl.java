package raft.cuhk;


import io.grpc.stub.StreamObserver;
import raft.Raft;
import raft.RaftNodeGrpc;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

public class RaftImpl extends RaftNodeGrpc.RaftNodeImplBase {
    final String[] replication_connection;
    final String localhost;
    final int lport;
    int nodeId;
    int heartBeatInterval;  //ms
    int electionTimeout;   //ms
    Raft.Role serverState;
    public BlockingQueue<Integer> resetQueue =new LinkedBlockingDeque<>();
    public BlockingQueue<Integer> electionResetQueue =new LinkedBlockingDeque<>();
    int currentTerm = 0;
    int votedFor = -1;


    public RaftImpl(String[] replication_connection, String localhost, int lport, int nodeId, int heartBeatInterval, int electionTimeout) {
        this.replication_connection = replication_connection;
        this.localhost = localhost;
        this.lport = lport;
        this.nodeId = nodeId;
        this.heartBeatInterval = heartBeatInterval;
        this.electionTimeout = electionTimeout;
        this.serverState = Raft.Role.Follower;
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
        int timeout = request.getTimeout();
        this.electionTimeout = timeout;
        try {
            this.resetQueue.put(99);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Raft.SetElectionTimeoutReply build = Raft.SetElectionTimeoutReply.newBuilder().build();
        responseObserver.onNext(build);
        responseObserver.onCompleted();


    }

    @Override
    public void setHeartBeatInterval(Raft.SetHeartBeatIntervalArgs request, StreamObserver<Raft.SetHeartBeatIntervalReply> responseObserver) {
        int timeout = request.getInterval();
        this.heartBeatInterval = timeout;
        try {
            this.resetQueue.put(99);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Raft.SetHeartBeatIntervalReply build = Raft.SetHeartBeatIntervalReply.newBuilder().build();
        responseObserver.onNext(build);
        responseObserver.onCompleted();

    }

    @Override
    public void requestVote(Raft.RequestVoteArgs request, StreamObserver<Raft.RequestVoteReply> responseObserver) {
        System.out.println(this.nodeId + " at term " + this.currentTerm + " recv request vote from "+request.getFrom() + " at " + request.getTerm());
        int term = request.getTerm();
        int from = request.getFrom();
        boolean voteGranted = false;
        if (term > this.currentTerm){
            System.out.println(this.nodeId + " at term " + this.currentTerm + " voted for "+request.getFrom() + " at " + request.getTerm());
//            this.currentTerm = term;
            votedFor = from;
            voteGranted = true;
            this.serverState = Raft.Role.Follower;
            try {
                this.resetQueue.put(99);
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
        int from = request.getFrom();
        int leaderId = request.getLeaderId();
        int term = request.getTerm();
        int to = request.getTo();
        System.out.println(this.nodeId + " recv appendEntries from " + from + " at " + term);
        this.currentTerm = term;
        try {
            this.resetQueue.put(99);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Raft.AppendEntriesReply appendEntriesReply = Raft.AppendEntriesReply.newBuilder().build();
        responseObserver.onNext(appendEntriesReply);
        responseObserver.onCompleted();
    }

    @Override
    public void checkEvents(Raft.CheckEventsArgs request, StreamObserver<Raft.CheckEventsReply> responseObserver) {
        super.checkEvents(request, responseObserver);
    }

    @Override
    public void whoAreYou(Raft.WhoAreYouArgs request, StreamObserver<Raft.WhoAreYouReply> responseObserver) {
        Raft.WhoAreYouReply whoAreYouReply = Raft.WhoAreYouReply.newBuilder()
                .setMsg(localhost + ":" + lport + "| state" + this.serverState + "| votedFor" + this.votedFor).build();
        responseObserver.onNext(whoAreYouReply);
        responseObserver.onCompleted();
    }
}

package com.cuhk.raft.core;

import com.cuhk.raft.pb.Raft;
import com.cuhk.raft.pb.RaftNodeGrpc;
import io.grpc.stub.StreamObserver;

public class RaftCore extends RaftNodeGrpc.RaftNodeImplBase{

    private final int nodeId; //表示该节点的id
    private final int heartBeatInterval;  //作为leader时，心跳间隔时间 单位ms
    private final int electionTimeout; //作为candidate时，选举超市时间 单位ms

    private int currentTerm = 0;
    private int  votedFor = -1;

    public RaftCore(int nodeId, int heartBeatInterval, int electionTimeout) {
        this.nodeId = nodeId;
        this.heartBeatInterval = heartBeatInterval;
        this.electionTimeout = electionTimeout;
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
        super.requestVote(request, responseObserver);
    }

    @Override
    public void appendEntries(Raft.AppendEntriesArgs request, StreamObserver<Raft.AppendEntriesReply> responseObserver) {
        super.appendEntries(request, responseObserver);
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

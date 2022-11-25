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
        Raft.WhoAreYouReply whoAreYouReply = Raft.WhoAreYouReply.newBuilder().setMsg(localhost + ":" + lport).build();
        responseObserver.onNext(whoAreYouReply);
        responseObserver.onCompleted();    }
}

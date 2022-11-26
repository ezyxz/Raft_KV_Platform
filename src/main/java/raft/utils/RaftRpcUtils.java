package raft.utils;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import raft.Raft;
import raft.RaftNodeGrpc;
import raft.cuhk.ConnConfig;

public class RaftRpcUtils {

    public static void propose() {

    }

    public static void getValue() {
    }

    public static void setElectionTimeout() {
    }

    public static void setHeartBeatInterval() {
    }

    public static Raft.RequestVoteReply requestVote(ConnConfig connConfig, Raft.RequestVoteArgs requestVoteArgs) {
        ManagedChannel channel = ManagedChannelBuilder.forAddress(connConfig.ip_address, connConfig.rport)
                .usePlaintext() // disable TLS
                .build();
        RaftNodeGrpc.RaftNodeBlockingStub stub = RaftNodeGrpc.newBlockingStub(channel);
        Raft.RequestVoteReply requestVoteReply = null;
        try {
            requestVoteReply = stub.requestVote(requestVoteArgs);
        }catch (StatusRuntimeException e){

        }
        channel.shutdownNow();
        return requestVoteReply;
    }

    public static Raft.AppendEntriesReply appendEntries(ConnConfig connConfig, Raft.AppendEntriesArgs appendEntriesArgs) {
        ManagedChannel channel = ManagedChannelBuilder.forAddress(connConfig.ip_address, connConfig.rport)
                .usePlaintext() // disable TLS
                .build();
        RaftNodeGrpc.RaftNodeBlockingStub stub = RaftNodeGrpc.newBlockingStub(channel);
        Raft.AppendEntriesReply appendEntriesReply = null;
        try {
            appendEntriesReply = stub.appendEntries(appendEntriesArgs);
        }catch (StatusRuntimeException e){

        }
        channel.shutdownNow();
        return appendEntriesReply;

    }

    public static void checkEvents() {
    }

    public static Raft.WhoAreYouReply whoAreYou(ConnConfig connConfig, Raft.WhoAreYouArgs whoAreYouArgs) {
        ManagedChannel channel = ManagedChannelBuilder.forAddress(connConfig.ip_address, connConfig.rport)
                .usePlaintext() // disable TLS
                .build();
        RaftNodeGrpc.RaftNodeBlockingStub stub = RaftNodeGrpc.newBlockingStub(channel);
        Raft.WhoAreYouReply whoAreYouReply = null;
        try {
            whoAreYouReply = stub.whoAreYou(whoAreYouArgs);
        }catch (StatusRuntimeException e){

        }
        channel.shutdownNow();
        return whoAreYouReply;

    }
}

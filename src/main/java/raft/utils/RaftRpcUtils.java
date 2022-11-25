package raft.utils;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;
import raft.Raft;
import raft.RaftNodeGrpc;
import raft.cuhk.ConnConfig;
import raft.cuhk.RaftImpl;

public class RaftRpcUtils {

    public static void propose() {

    }

    public static void getValue() {
    }

    public static void setElectionTimeout() {
    }

    public static void setHeartBeatInterval() {
    }

    public static void requestVote() {
    }

    public static void appendEntries() {
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

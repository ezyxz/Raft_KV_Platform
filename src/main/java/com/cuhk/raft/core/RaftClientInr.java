package com.cuhk.raft.core;

import com.cuhk.raft.bean.RaftStateBean;
import com.cuhk.raft.bean.ReplicatorBean;
import com.cuhk.raft.pb.Raft;
import com.cuhk.raft.pb.RaftNodeGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;

import java.util.concurrent.TimeUnit;

public class RaftClientInr {

    //对方server的node id
    private final int nodeId;
    //对方server的host
    private final String host;
    //对方server的port
    private final int port;

    private  ManagedChannel channel;
    private  RaftNodeGrpc.RaftNodeBlockingStub blockingStub;

    public RaftClientInr(ReplicatorBean replicatorBean) {
        this.nodeId = replicatorBean.getId();
        this.host = replicatorBean.getAddress().split(":")[0];
        this.port = Integer.parseInt(replicatorBean.getAddress().split(":")[1]);

    }
    public Raft.RequestVoteReply requestVote(RaftStateBean raftStateBean) {

        channel = ManagedChannelBuilder.forAddress(host, port)
                .usePlaintext() // disable TLS
                .build();
        blockingStub = RaftNodeGrpc.newBlockingStub(channel);

        Raft.RequestVoteArgs requestVoteArgs = Raft.RequestVoteArgs.newBuilder()
                .setCandidateId(raftStateBean.getNodeId())
                .setTerm(raftStateBean.getCurrentTerm())
                .setTo(nodeId)
                .setFrom(raftStateBean.getNodeId())
                .setLastLogIndex(0)
                .setLastLogTerm(0).build();

        Raft.RequestVoteReply requestVoteReply = null;
        try {
            requestVoteReply = blockingStub.requestVote(requestVoteArgs);
        }catch (StatusRuntimeException e){

        }
        channel.shutdownNow();
        return requestVoteReply;
    }

    public Raft.AppendEntriesReply heatBeat(RaftStateBean raftStateBean) {

        channel = ManagedChannelBuilder.forAddress(host, port)
                .usePlaintext() // disable TLS
                .build();
        blockingStub = RaftNodeGrpc.newBlockingStub(channel);

        Raft.AppendEntriesArgs appendEntriesArgs = Raft.AppendEntriesArgs.newBuilder()
                .setFrom(raftStateBean.getNodeId())
                .setLeaderId(raftStateBean.getNodeId())
                .setTo(nodeId)
                .setTerm(raftStateBean.getCurrentTerm())
                .setPrevLogTerm(0)
                .setPrevLogTerm(0)
                .setLeaderCommit(raftStateBean.getCommitIndex())
                .build();
        Raft.AppendEntriesReply appendEntriesReply = null;
        try {
            appendEntriesReply = blockingStub.appendEntries(appendEntriesArgs);
        }catch (StatusRuntimeException e){

        }
        channel.shutdownNow();
        return appendEntriesReply;
    }

}

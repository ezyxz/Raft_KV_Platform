package com.cuhk.raft.core;

import com.cuhk.raft.bean.ReplicatorBean;
import com.cuhk.raft.pb.Raft;
import com.cuhk.raft.pb.RaftNodeGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import java.util.concurrent.TimeUnit;

public class RaftClientInr {

    private final int nodeId;
    private final String host;
    private final int port;

    private  ManagedChannel channel;
    private  RaftNodeGrpc.RaftNodeBlockingStub blockingStub;

    public RaftClientInr(ReplicatorBean replicatorBean) {
        this.nodeId = replicatorBean.getId();
        this.host = replicatorBean.getAddress().split(":")[0];
        this.port = Integer.parseInt(replicatorBean.getAddress().split(":")[1]);
        //初始化连接
        channel = ManagedChannelBuilder.forAddress(host, port)
                .usePlaintext()
                .build();
        //初始化远程服务Stub
        blockingStub = RaftNodeGrpc.newBlockingStub(channel);
    }
    public void shutdown() throws InterruptedException {
        //关闭连接
        channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
    }

    public Raft.GetValueReply getValue(String key){
        Raft.GetValueArgs getValueArgs = Raft.GetValueArgs.newBuilder().setKey(key).build();
        Raft.GetValueReply value = blockingStub.getValue(getValueArgs);
        return value;
    }
    public Raft.ProposeReply putValue(String key, String value){
        Raft.ProposeArgs proposeArgs = Raft.ProposeArgs.newBuilder()
                .setKey(key)
                .setV(value)
                .setOp(Raft.Operation.Put).build();
        Raft.ProposeReply reply = blockingStub.propose(proposeArgs);
        return reply;
    }



    public Raft.WhoAreYouReply whoAreYou(){
        Raft.WhoAreYouArgs build = Raft.WhoAreYouArgs.newBuilder().build();

        Raft.WhoAreYouReply whoAreYouReply = blockingStub.whoAreYou(build);
        return whoAreYouReply;
    }

    public Raft.ProposeReply deleteValue(String key){
        Raft.ProposeArgs proposeArgs = Raft.ProposeArgs.newBuilder()
                .setKey(key)
                .setOp(Raft.Operation.Delete).build();
        Raft.ProposeReply reply = blockingStub.propose(proposeArgs);
        return reply;
    }

}

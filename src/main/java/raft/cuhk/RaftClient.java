package raft.cuhk;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import raft.Raft;
import raft.RaftNodeGrpc;

import java.net.ConnectException;
import java.util.concurrent.TimeUnit;

public class RaftClient {
    private final ManagedChannel channel;
    private final RaftNodeGrpc.RaftNodeBlockingStub blockingStub;

    public RaftClient(String host, int port) {
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
    public String WhoAreYou () {
        //构造服务调用参数对象
        Raft.WhoAreYouArgs whoAreYouArgs = Raft.WhoAreYouArgs.newBuilder().setMsg("hello").build();
        //调用远程服务方法
        Raft.WhoAreYouReply whoAreYouReply = blockingStub.whoAreYou(whoAreYouArgs);
        //返回值
        return whoAreYouReply.getMsg();
    }
    public void setElectionTimeOut (int timeOut) {
        //构造服务调用参数对象
        Raft.SetElectionTimeoutArgs build = Raft.SetElectionTimeoutArgs.newBuilder().setTimeout(timeOut).build();
        //调用远程服务方法
        blockingStub.setElectionTimeout(build);
        //返回值
        return;
    }
    public void testInsert(){
        Raft.ProposeArgs proposeArgs = Raft.ProposeArgs.newBuilder()
                .setKey("admin3")
                .setV("999")
                .setOp(Raft.Operation.Put).build();
        Raft.ProposeReply reply = blockingStub.propose(proposeArgs);
        System.out.println(reply.getStatus());
    }

    public void testGetValue(){
        Raft.GetValueArgs admin = Raft.GetValueArgs.newBuilder().setKey("admin3").build();
        Raft.GetValueReply value = blockingStub.getValue(admin);
        System.out.println(value.getStatus() + " " + value.getV());
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

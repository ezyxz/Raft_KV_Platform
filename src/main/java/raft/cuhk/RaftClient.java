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
    public static void main(String[] args) throws InterruptedException {
        RaftClient client = new RaftClient("127.0.0.1", 5002);
        //服务调用
        String content = "";
        try {
            content = client.WhoAreYou();
            //打印调用结果

        }catch (StatusRuntimeException e){

        }
        System.out.println(content);
        //关闭连接
        client.shutdown();
    }
}

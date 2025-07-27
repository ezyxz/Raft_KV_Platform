package com.cuhk.raft.test;

import com.cuhk.raft.pb.Raft;
import com.cuhk.raft.pb.RaftNodeGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class TestClient {

    private final ManagedChannel channel;
    private final RaftNodeGrpc.RaftNodeBlockingStub blockingStub;
    private static final int port = 50051;

    public TestClient(String host, int port) {
        // 创建一个 gRPC 通道，连接到服务器
        channel = ManagedChannelBuilder.forAddress(host, port)
                .usePlaintext()  // 不使用加密
                .build();
        blockingStub = RaftNodeGrpc.newBlockingStub(channel);  // 创建同步存根
    }
    // 发送 Propose 请求
    public void propose(String key, String value, Raft.Operation operation) {
        Raft.ProposeArgs args = Raft.ProposeArgs.newBuilder()
                .setKey(key)
                .setV(value)
                .setOp(operation)
                .build();

        try {
            Raft.ProposeReply reply = blockingStub.propose(args);
            System.out.println("Propose result: " + reply.getStatus() + ", Leader ID: " + reply.getCurrentLeader());
        } catch (Exception e) {
            System.err.println("Propose failed: " + e.getMessage());
        }
    }
    public static void main(String[] args) {
        TestClient client = new TestClient("127.0.0.1", port);
        client.propose("hello","world",Raft.Operation.Put);

    }
}

package com.cuhk.raft.test;

import com.cuhk.raft.pb.Raft;
import com.cuhk.raft.pb.RaftNodeGrpc;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;

import java.io.IOException;

public class TestServer {

    private static final int port = 50051;

    public static void main(String[] args) throws InterruptedException, IOException {
        Server server = ServerBuilder
                .forPort(port)
                .addService(new RaftNodeServiceImpl())
                .build();
        server.start();
        System.out.println("Server started, listening on " + port);
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                if (server != null) {
                    server.shutdown();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }));
        server.awaitTermination();
    }
    static class RaftNodeServiceImpl extends RaftNodeGrpc.RaftNodeImplBase {
        // Propose 请求处理
        @Override
        public void propose(Raft.ProposeArgs request, StreamObserver<Raft.ProposeReply> responseObserver) {
            System.out.println("Received Propose request: Key=" + request.getKey() + ", Value=" + request.getV());

            // 简单模拟 Raft 协议处理：如果是 Leader，则返回 OK
            Raft.ProposeReply reply = Raft.ProposeReply.newBuilder()
                    .setCurrentLeader(1)  // 假设当前的 Leader 是 1
                    .setStatus(Raft.Status.OK)  // 操作成功
                    .build();

            responseObserver.onNext(reply);
            responseObserver.onCompleted();
        }
    }


}

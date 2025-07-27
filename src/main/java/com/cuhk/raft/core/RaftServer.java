package com.cuhk.raft.core;

import com.cuhk.raft.pb.RaftNodeGrpc;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class RaftServer {

    Logger logger = Logger.getLogger(RaftServer.class);


    private Server server;
    private final int port;

    public RaftServer(int port) {
        this.port = port;
    }

    public RaftServer(Server server, int port) {
        this.server = server;
        this.port = port;
    }

    public void build(RaftNodeGrpc.RaftNodeImplBase node) {
        if (server == null) {
            throw new NullPointerException("RaftNodeGrpc.RaftNodeImplBase is null");
        }
        this.server = ServerBuilder.forPort(this.port)
                .addService(node)
                .build();
        logger.info("server build successful");
    }


    public void start() throws IOException {
        if (this.server == null) {throw new NullPointerException("server is null");}
        this.server.start();
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                // Use stderr here since the logger may have been reset by its JVM shutdown hook.
                System.err.println("*** shutting down gRPC server since JVM is shutting down");
                try {
                    RaftServer.this.stop();
                } catch (InterruptedException e) {
                    e.printStackTrace(System.err);
                }
                System.err.println("*** server shut down");
            }
        });
    }

    private void stop() throws InterruptedException {
        if (server != null) {
            server.shutdown().awaitTermination(30, TimeUnit.SECONDS);
        }
    }

    public void blockUntilShutdown() throws InterruptedException {
        if (server != null) {
            server.awaitTermination();
        }
    }
}

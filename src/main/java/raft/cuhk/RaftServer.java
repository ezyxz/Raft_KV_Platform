package raft.cuhk;

import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class RaftServer {
    private Server server;
    final String[] replication_connection;
    final String localhost;
    final int lport;

    public RaftServer(String[] replication_connection, String localhost, int lport) {
        this.replication_connection = replication_connection;
        this.localhost = localhost;
        this.lport = lport;
    }


    public void start(RaftImpl Node) throws IOException {
        /* The port on which the server should run */
        int port = lport;
        server = ServerBuilder.forPort(port)
                .addService(Node)  //这里可以添加多个模块
                .build()
                .start();
        System.out.println("Server started, listening on " + port);
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

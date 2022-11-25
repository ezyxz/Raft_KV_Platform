package raft.cuhk;

import io.grpc.Channel;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import raft.Raft;
import raft.RaftNodeGrpc;
import raft.utils.RaftRpcUtils;

import java.io.*;
import java.net.URL;
import java.util.*;

public class RaftMain {
    static String[] replication_connection;
    static String localhost;
    static int lport;
    static RaftServer server;
    static Map<Integer, ConnConfig> hostConnectionMap;
    static int NodeId;


    public static void main(String[] args) throws InterruptedException {
        System.out.println("Raft-KV-Platform starts....");
        NodeId = Integer.parseInt(args[1]);
        load_config(NodeId);
        System.out.println("Load config successful....");
        RaftImpl Node = new RaftImpl(replication_connection, localhost, lport);
        new Thread(new Runnable() {
            @Override
            public void run() {
                start_service(Node);
            }
        }).start();
        System.out.println("Local Services successful....");
        hostConnectionMap = new HashMap<>();
        int i = 1;
        for (String rep : replication_connection){
            String[] ss = rep.split(":");
            if (localhost.equals(ss[0]) && Integer.parseInt(ss[1]) == lport){
                i++;
                continue;
            }
//            ManagedChannel channel = ManagedChannelBuilder.forAddress(ss[0], Integer.parseInt(ss[1]))
//                    .usePlaintext() // disable TLS
//                    .build();

            hostConnectionMap.put(
                    i, new ConnConfig(ss[0], Integer.parseInt(ss[1]))
            );
            i++;
        }
        System.out.println("Connection Replication successful....");

        Thread.sleep(15000);
        for (int j = 0; j < 100000; j++) {
            for (ConnConfig connConfig: hostConnectionMap.values()){
                Raft.WhoAreYouArgs whoAreYouArgs = Raft.WhoAreYouArgs.newBuilder().setMsg("hello").build();
                Raft.WhoAreYouReply whoAreYouReply = RaftRpcUtils.whoAreYou(connConfig, whoAreYouArgs);
                if (whoAreYouReply != null)
                    System.out.println(whoAreYouReply.getMsg() + " " + j);
            }
//            Thread.sleep(10);
        }

    }

    static void start_service(RaftImpl Node){
        server = new RaftServer(replication_connection, localhost, lport);
        try {
            server.start(Node);
            server.blockUntilShutdown();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }


    }
    static void load_config(int nodeId){
        URL url = RaftMain.class.getClassLoader().getResource("config.properties");
        if (url != null) {
            String fileName = url.getFile();
            InputStream in = null;
            try {
                in = new BufferedInputStream(new FileInputStream(fileName));
                Properties properties = new Properties();
                properties.load(in);
                int conn = Integer.parseInt(properties.getProperty("replication"));
                replication_connection = new String[conn];
                for (int i = 1; i <= conn; i++) {
                    if (nodeId == i){
                        String[] ss = properties.getProperty("replicator"+i).split(":");
                        localhost = ss[0];
                        lport = Integer.parseInt(ss[1]);
                    }
                    replication_connection[i-1] = properties.getProperty("replicator"+i);
                }


            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}

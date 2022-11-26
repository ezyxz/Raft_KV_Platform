package raft.cuhk;

import raft.Raft;
import raft.utils.RaftRpcUtils;
import java.lang.Object;
import java.io.*;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

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
        RaftImpl Node = new RaftImpl(replication_connection, localhost, lport, NodeId, 1000 ,3000);
        new Thread(new Runnable() {
            @Override
            public void run() {
                start_service(Node);
            }
        }).start();
        System.out.println("Local Services successful....");
        hostConnectionMap = new HashMap<>();
        for (int i = 0; i < replication_connection.length; i++){
            String[] ss = replication_connection[i].split(":");
            if (localhost.equals(ss[0]) && Integer.parseInt(ss[1]) == lport){
                continue;
            }
            hostConnectionMap.put(
                    i+1, new ConnConfig(ss[0], Integer.parseInt(ss[1]))
            );
        }
        System.out.println("Connection Replication successful....");
        System.out.println("Start running.....");
        while(true){

            switch (Node.serverState){

                case Candidate:
                    System.out.println(NodeId + " becomes candidate at " + Node.currentTerm);
                    Node.currentTerm++;
                    Node.votedFor = NodeId;
                    AtomicReference<Integer> voteNum = new AtomicReference<>(0);
                    for (int hostId : hostConnectionMap.keySet()){
                        ConnConfig connConfig = hostConnectionMap.get(hostId);
                        new Thread(() -> {
                            System.out.println(NodeId + " ask vote for " + hostId);
                            Raft.RequestVoteArgs requestVoteArgs = Raft.RequestVoteArgs.newBuilder()
                                    .setCandidateId(NodeId)
                                    .setTerm(Node.currentTerm)
                                    .setTo(hostId)
                                    .setFrom(NodeId)
                                    .setLastLogIndex(0)
                                    .setLastLogTerm(0).build();
                            Raft.RequestVoteReply requestVoteReply = RaftRpcUtils.requestVote(connConfig, requestVoteArgs);
                            if (requestVoteReply != null && requestVoteReply.getVoteGranted() && requestVoteArgs.getTerm() == Node.currentTerm){
                                System.out.println(NodeId + " granted from " + requestVoteReply.getFrom());
                                voteNum.getAndSet(voteNum.get() + 1);
                                if (voteNum.get() == hostConnectionMap.size()/2 && Node.serverState == Raft.Role.Candidate){
                                    Node.serverState = Raft.Role.Leader;
                                    try {
                                        Node.resetQueue.put(88);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }).start();
                    }
                    Integer poll = Node.resetQueue.poll(Node.electionTimeout, TimeUnit.MILLISECONDS);
                    if (poll != null){
                        if (poll == 88){

                        }else if (poll == 99){
                            Node.serverState = Raft.Role.Follower;
                        }
                        //Time out
                    }else{
                        Node.electionTimeout = 3000 + (int)(Math.random()*1000);
                        System.out.println(NodeId + " reset electionTimeout as " + Node.electionTimeout);
                    }
                    break;

                case Follower:
                    System.out.println(NodeId + " becomes follower at " + Node.currentTerm);
                    Integer poll_f = Node.resetQueue.poll(Node.electionTimeout, TimeUnit.MILLISECONDS);
                    if (poll_f == null){
                        Node.serverState = Raft.Role.Candidate;
                    }
                    break;


                case Leader:
                    System.out.println(NodeId + " becomes leader at " + Node.currentTerm);
                    System.out.println("First heart beat interval");
                    for (int hostId : hostConnectionMap.keySet()){
                        ConnConfig connConfig = hostConnectionMap.get(hostId);
                        new Thread(() -> {
                            Raft.AppendEntriesArgs appendEntriesArgs = Raft.AppendEntriesArgs.newBuilder()
                                    .setFrom(Node.nodeId)
                                    .setLeaderId(Node.nodeId)
                                    .setTo(hostId)
                                    .setTerm(Node.currentTerm)
                                    .setPrevLogTerm(0)
                                    .setPrevLogTerm(0)
                                    .build();
                            RaftRpcUtils.appendEntries(connConfig, appendEntriesArgs);
                        }).start();
                    }
                    while(Node.serverState == Raft.Role.Leader){
                        Integer poll_l = Node.resetQueue.poll(Node.heartBeatInterval, TimeUnit.MILLISECONDS);
                        if (poll_l != null){
                            continue;
                        }
                        System.out.println("Normal heart beat interval");
                        for (int hostId : hostConnectionMap.keySet()){
                            ConnConfig connConfig = hostConnectionMap.get(hostId);
                            new Thread(() -> {
                                Raft.AppendEntriesArgs appendEntriesArgs = Raft.AppendEntriesArgs.newBuilder()
                                        .setFrom(Node.nodeId)
                                        .setLeaderId(Node.nodeId)
                                        .setTo(hostId)
                                        .setTerm(Node.currentTerm)
                                        .setPrevLogTerm(0)
                                        .setPrevLogTerm(0)
                                        .build();
                                RaftRpcUtils.appendEntries(connConfig, appendEntriesArgs);
                            }).start();
                        }
                    }
                    break;
            }
        }

//        Thread.sleep(15000);
//        for (int j = 0; j < 100000; j++) {
//            for (ConnConfig connConfig: hostConnectionMap.values()){
//                Raft.WhoAreYouArgs whoAreYouArgs = Raft.WhoAreYouArgs.newBuilder().setMsg("hello").build();
//                Raft.WhoAreYouReply whoAreYouReply = RaftRpcUtils.whoAreYou(connConfig, whoAreYouArgs);
//                if (whoAreYouReply != null)
//                    System.out.println(whoAreYouReply.getMsg() + " " + j);
//            }
//        }
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

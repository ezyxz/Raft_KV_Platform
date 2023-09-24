package raft.cuhk;

import raft.Raft;
import raft.utils.Param;
import raft.utils.RaftRpcUtils;
import java.lang.Object;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class RaftMain {
    static String[] replication_connection;
    static String localhost;
    static int lport;
    static RaftServer server;
    static Map<Integer, ConnConfig> hostConnectionMap;
    static int NodeId;
    static Object lock = new Object();


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
                    System.out.println(NodeId + " becomes candidate at Term" + Node.currentTerm);
                    Node.currentTerm++;
                    Node.votedFor = NodeId;
                    AtomicReference<Integer> voteNum = new AtomicReference<>(0);
                    for (int hostId : hostConnectionMap.keySet()){
                        ConnConfig connConfig = hostConnectionMap.get(hostId);
                        new Thread(() -> {
                            System.out.println(NodeId + " ask vote for id " + hostId);
                            Raft.RequestVoteArgs requestVoteArgs = Raft.RequestVoteArgs.newBuilder()
                                    .setCandidateId(NodeId)
                                    .setTerm(Node.currentTerm)
                                    .setTo(hostId)
                                    .setFrom(NodeId)
                                    .setLastLogIndex(0)
                                    .setLastLogTerm(0).build();
                            Raft.RequestVoteReply requestVoteReply = RaftRpcUtils.requestVote(connConfig, requestVoteArgs);
                            if (requestVoteReply != null && requestVoteReply.getVoteGranted() && requestVoteArgs.getTerm() == Node.currentTerm){
                                System.out.println(NodeId + " granted from id " + requestVoteReply.getFrom());
                                voteNum.getAndSet(voteNum.get() + 1);
                                if (voteNum.get() == hostConnectionMap.size()/2 && Node.serverState == Raft.Role.Candidate){
                                    Node.serverState = Raft.Role.Leader;
                                    try {
                                        Node.resetQueue.put(Param.REST_BECOME_LEADER);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }).start();
                    }
                    Integer poll = Node.resetQueue.poll(Node.electionTimeout, TimeUnit.MILLISECONDS);
                    if (poll != null){
                        if (poll == Param.REST_ALREADY_VOTED){
                            Node.serverState = Raft.Role.Follower;
                        }
                        //Time out
                    }else{
                        Node.electionTimeout = 3000 + (int)(Math.random()*1000);
                        System.out.println(NodeId + " reset electionTimeout as " + Node.electionTimeout + " ms");
                    }


                    break;

                case Follower:
                    System.out.println(NodeId + " becomes follower at Term " + Node.currentTerm);
                    Integer poll_f = Node.resetQueue.poll(Node.electionTimeout, TimeUnit.MILLISECONDS);
                    if (poll_f == null){
                        Node.serverState = Raft.Role.Candidate;
                    }
                    break;


                case Leader:
                    System.out.println(NodeId + " becomes leader at Term " + Node.currentTerm);
                    System.out.println(NodeId +" leader First heart beat interval");
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
                                    .setLeaderCommit(Node.commitIndex)
                                    .build();
                            RaftRpcUtils.appendEntries(connConfig, appendEntriesArgs);
                        }).start();
                    }
                    while(Node.serverState == Raft.Role.Leader){
                        Integer poll_l = Node.resetQueue.poll(Node.heartBeatInterval, TimeUnit.MILLISECONDS);
                        if (poll_l != null){
                            continue;
                        }
                        System.out.println(NodeId +" leader Normal heart beat interval");
                        int leaderCommit = Node.commitIndex;
                        AtomicInteger successNum = new AtomicInteger();
                        for (int hostId : hostConnectionMap.keySet()){
                            ConnConfig connConfig = hostConnectionMap.get(hostId);
                            new Thread(() -> {
                                Raft.AppendEntriesArgs appendEntriesArgs;
                                if (Node.NodeLogMatch.containsKey((hostId))){
                                    int prevlog = Node.NodeLogMatch.get(hostId);
                                    System.out.println(NodeId + " Leader send prev logs at prevlog idx " + prevlog + " to "+hostId);
                                    if (prevlog < 0){
                                        appendEntriesArgs = Raft.AppendEntriesArgs.newBuilder()
                                                .setFrom(Node.nodeId)
                                                .setLeaderId(Node.nodeId)
                                                .setTo(hostId)
                                                .setTerm(Node.currentTerm)
                                                .setPrevLogIndex(Node.log.size()-1)
                                                .setPrevLogTerm(0)
                                                .addAllEntries(Node.log)
                                                .setLeaderCommit(Node.commitIndex)
                                                .build();
                                    }else{
                                        appendEntriesArgs = Raft.AppendEntriesArgs.newBuilder()
                                                .setFrom(Node.nodeId)
                                                .setLeaderId(Node.nodeId)
                                                .setTo(hostId)
                                                .setTerm(Node.currentTerm)
                                                .setPrevLogIndex(Node.log.size()-1)
                                                .setLeaderCommit(Node.commitIndex)
                                                .setPrevLogTerm(Node.log.get(prevlog).getTerm())
                                                .addAllEntries(Node.log.subList(prevlog, Node.log.size()))
                                                .build();
                                    }

                                    Node.NodeLogMatch.remove(hostId);
                                }else {
                                    if (Node.commitIndex < Node.log.size()-1) {
                                        System.out.println(NodeId + " send new logs to "+hostId);
//                                        System.out.println("--->"+(Node.log.size() - 1));
                                        appendEntriesArgs = Raft.AppendEntriesArgs.newBuilder()
                                                .setFrom(Node.nodeId)
                                                .setLeaderId(Node.nodeId)
                                                .setTo(hostId)
                                                .setTerm(Node.currentTerm)
                                                .setPrevLogTerm(Node.lastLogTerm)
                                                .setPrevLogIndex(Node.log.size() - 1)
//                                                .addEntries(Node.log.get(Node.commitIndex+1))
                                                .addAllEntries(Node.log.subList(Node.commitIndex+1, Node.log.size()))
                                                .setLeaderCommit(Node.commitIndex)
                                                .build();
                                    } else {
                                        appendEntriesArgs = Raft.AppendEntriesArgs.newBuilder()
                                                .setFrom(Node.nodeId)
                                                .setLeaderId(Node.nodeId)
                                                .setTo(hostId)
                                                .setTerm(Node.currentTerm)
                                                .setPrevLogTerm(Node.lastLogTerm)
                                                .setPrevLogIndex(Node.log.size() - 1)
                                                .setLeaderCommit(Node.commitIndex)
                                                .build();
                                    }
                                }

                                Raft.AppendEntriesReply appendEntriesReply = RaftRpcUtils.appendEntries(connConfig, appendEntriesArgs);
                                if (appendEntriesReply != null && appendEntriesReply.getSuccess() && Node.commitIndex < Node.log.size()-1){
                                    synchronized (lock){
                                        successNum.getAndIncrement();
                                        System.out.println(NodeId + " Leader commit get  votes with total " + successNum);
                                        if (successNum.get() >= hostConnectionMap.size()/2 &&  Node.semaphoreMap.get(leaderCommit) != null){
                                            Node.semaphoreMap.get(leaderCommit).release(appendEntriesArgs.getEntriesCount());
                                        }
                                    }
                                }else if(appendEntriesReply != null && !appendEntriesReply.getSuccess()){
                                    System.out.println(NodeId + " Leader append entries reject from" + appendEntriesReply.getFrom());
                                    Node.NodeLogMatch.put(appendEntriesReply.getFrom(), appendEntriesReply.getMatchIndex());
                                }
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

        try {
//            BufferedReader bufferedReader = new BufferedReader(new FileReader("src/main/resources/config.properties"));
            BufferedReader bufferedReader;
            try{
                bufferedReader = new BufferedReader(new FileReader("../conf/config.properties"));
            }catch (Exception e){
                bufferedReader = new BufferedReader(new FileReader("conf/config.properties"));
            }

            Properties properties = new Properties();
            properties.load(bufferedReader);
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

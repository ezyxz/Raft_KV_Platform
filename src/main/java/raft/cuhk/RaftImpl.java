package raft.cuhk;


import io.grpc.stub.StreamObserver;
import raft.Raft;
import raft.RaftNodeGrpc;
import raft.utils.Param;

import java.sql.ParameterMetaData;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.List;
import java.util.concurrent.Semaphore;

public class RaftImpl extends RaftNodeGrpc.RaftNodeImplBase {
    final String[] replication_connection;
    final String localhost;
    final int lport;
    int nodeId;
    int heartBeatInterval;  //ms
    int electionTimeout;   //ms
    Raft.Role serverState;
    int commitIndex = -1;
    int LeaderCommit = 0;
    public BlockingQueue<Integer> resetQueue =new LinkedBlockingDeque<>();
    public BlockingQueue<Integer> electionResetQueue =new LinkedBlockingDeque<>();
    Object lock = new Object();
    int currentTerm = 0;
    int votedFor = -1;
    Map<String, Integer> kvstore = new ConcurrentHashMap<>();
    Map<Integer, Semaphore> semaphoreMap = new ConcurrentHashMap<>();
    List<Raft.LogEntry> log = new ArrayList<>();
    public Map<Integer, Integer> NodeLogMatch = new ConcurrentHashMap<>();
    int lastLogTerm = 0;

    public RaftImpl(String[] replication_connection, String localhost, int lport, int nodeId, int heartBeatInterval, int electionTimeout) {
        this.replication_connection = replication_connection;
        this.localhost = localhost;
        this.lport = lport;
        this.nodeId = nodeId;
        this.heartBeatInterval = heartBeatInterval;
        this.electionTimeout = electionTimeout;
        this.serverState = Raft.Role.Follower;
    }

    @Override
    public void propose(Raft.ProposeArgs request, StreamObserver<Raft.ProposeReply> responseObserver) {
//        super.propose(request, responseObserver);
        System.out.println("Receive Client propose");

        int curr_leader = -1;
        Raft.Status status = Raft.Status.OK;

        if (this.serverState == Raft.Role.Leader){
            curr_leader = this.nodeId;
            if (request.getOp() == Raft.Operation.Delete){
                if ( ! this.kvstore.containsKey(request.getKey())){
                    status = Raft.Status.KeyNotFound;
                }
            }
        }else{
            curr_leader = this.votedFor;
            status = Raft.Status.WrongNode;
        }
        if (status == Raft.Status.OK || status == Raft.Status.KeyNotFound){
            Raft.LogEntry build = Raft.LogEntry.newBuilder().setTerm(this.currentTerm)
                    .setOp(request.getOp())
                    .setKey(request.getKey())
                    .setValue(request.getV()).build();
            int log_idx = this.commitIndex;
            log.add(build);
            System.out.println("Add to log, waiting to commit...");
            Semaphore semaphore = new Semaphore(0);
            semaphoreMap.put(log_idx, semaphore);
            try {
                semaphore.acquire();
                synchronized (lock){
                    System.out.println("start to commit...");
                    semaphoreMap.remove(log_idx);
                    if (request.getOp() == Raft.Operation.Put){
                        System.out.println("put " + request.getKey() + " " + request.getV());
                        this.kvstore.put(request.getKey(), request.getV());
                    }else{
                        if (this.kvstore.containsKey(request.getKey())){
                            this.kvstore.remove(request.getKey());
                        }
                    }
                    this.commitIndex++;
                    this.lastLogTerm = this.currentTerm;
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }


        Raft.ProposeReply build = Raft.ProposeReply.newBuilder()
                .setCurrentLeader(curr_leader)
                .setStatusValue(commitIndex)
                .setStatus(status)
                .build();
        responseObserver.onNext(build);
        responseObserver.onCompleted();

    }

    @Override
    public void getValue(Raft.GetValueArgs request, StreamObserver<Raft.GetValueReply> responseObserver) {
        Raft.Status status;
        if(kvstore.containsKey(request.getKey())){
            Raft.GetValueReply build = Raft.GetValueReply.newBuilder()
                    .setStatus(Raft.Status.KeyFound)
                    .setV(kvstore.get(request.getKey())).build();
            responseObserver.onNext(build);
            responseObserver.onCompleted();
        }else{
            Raft.GetValueReply build = Raft.GetValueReply.newBuilder()
                    .setStatus(Raft.Status.KeyNotFound).
                    build();
            responseObserver.onNext(build);
            responseObserver.onCompleted();
        }

    }

    @Override
    public void setElectionTimeout(Raft.SetElectionTimeoutArgs request, StreamObserver<Raft.SetElectionTimeoutReply> responseObserver) {
        int timeout = request.getTimeout();
        this.electionTimeout = timeout;
        try {
            this.resetQueue.put(Param.REST_ELECTION_TIME_OUT);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Raft.SetElectionTimeoutReply build = Raft.SetElectionTimeoutReply.newBuilder().build();
        responseObserver.onNext(build);
        responseObserver.onCompleted();


    }

    @Override
    public void setHeartBeatInterval(Raft.SetHeartBeatIntervalArgs request, StreamObserver<Raft.SetHeartBeatIntervalReply> responseObserver) {
        int timeout = request.getInterval();
        this.heartBeatInterval = timeout;
        try {
            this.resetQueue.put(Param.REST_HEART_BEAT_INTERVAL);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Raft.SetHeartBeatIntervalReply build = Raft.SetHeartBeatIntervalReply.newBuilder().build();
        responseObserver.onNext(build);
        responseObserver.onCompleted();

    }

    @Override
    public void requestVote(Raft.RequestVoteArgs request, StreamObserver<Raft.RequestVoteReply> responseObserver) {
        System.out.println(this.nodeId + " at term " + this.currentTerm + " recv request vote from "+request.getFrom() + " at " + request.getTerm());
        int term = request.getTerm();
        int from = request.getFrom();
        boolean voteGranted = false;
        if (term > this.currentTerm){
            System.out.println(this.nodeId + " at term " + this.currentTerm + " voted for "+request.getFrom() + " at " + request.getTerm());
//            this.currentTerm = term;
            votedFor = from;
            voteGranted = true;
            this.serverState = Raft.Role.Follower;
            try {
                this.resetQueue.put(Param.REST_ALREADY_VOTED);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Raft.RequestVoteReply requestVoteReply = Raft.RequestVoteReply.newBuilder()
                .setTo(from)
                .setFrom(this.nodeId)
                .setTerm(this.currentTerm)
                .setVoteGranted(voteGranted)
                .build();
        responseObserver.onNext(requestVoteReply);
        responseObserver.onCompleted();
    }

    @Override
    public void appendEntries(Raft.AppendEntriesArgs request, StreamObserver<Raft.AppendEntriesReply> responseObserver) {
        synchronized (lock){
            boolean rSuccess = false;
            int rterm = this.currentTerm;
            int from = request.getFrom();
            int leaderId = request.getLeaderId();
            int term = request.getTerm();
            int to = request.getTo();
            System.out.println(this.nodeId + " recv appendEntries from " + from + " at " + term);
            System.out.println(this.nodeId + " " +request.getLeaderCommit() +" | " + request.getPrevLogIndex() + "|" + request.getEntriesList().size() );
            if ( term < this.currentTerm){
                Raft.AppendEntriesReply appendEntriesReply = Raft.AppendEntriesReply.newBuilder()
                        .setFrom(nodeId)
                        .setTerm(this.currentTerm)
                        .setSuccess(false).build();
                responseObserver.onNext(appendEntriesReply);
                responseObserver.onCompleted();
                return;
            }
            if (term > this.currentTerm){
                this.currentTerm = term;
                rterm = term;
            }

            try {
                this.resetQueue.put(Param.REST_ELECTION_TIME_OUT);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            int lastLogIdx = this.log.size() - 1 ;
            //|| ( lastLogIdx > -1 && request.getPrevLogTerm() != this.log.get(lastLogIdx).getTerm()
            if (request.getPrevLogIndex() - request.getEntriesList().size() != lastLogIdx ){
                System.out.println("unmatch prev log");
                Raft.AppendEntriesReply appendEntriesReply = Raft.AppendEntriesReply.newBuilder()
                        .setFrom(nodeId)
                        .setTerm(this.currentTerm)
                        .setMatchIndex(lastLogIdx)
                        .setSuccess(false).build();
                responseObserver.onNext(appendEntriesReply);
                responseObserver.onCompleted();
                return;
            }
            rSuccess = true;
            List<Raft.LogEntry> entries = request.getEntriesList();
//            int index = request.getPrevLogIndex();
//            for (Raft.LogEntry entry : entries) {
//                index++;
//                if (index < this.log.size()){
//                    if (this.log.get(index).getTerm() == entry.getTerm()){
//                        continue;
//                    }
//                    this.log = this.log.subList(0, index);
//                }
//                this.log.addAll(entries);
//                break;
//            }
            if (entries.size() > 0){
                this.log.addAll(entries);
                System.out.println("Add log entry successful");
            }

            //commit
//            System.out.println("localcommit " + this.commitIndex + "leadercommit "+ request.getLeaderCommit());
            while (commitIndex < log.size() - 1 &&  this.commitIndex < request.getLeaderCommit()){
                commitIndex++;
                System.out.println("commit success at commit index"+commitIndex);
                if (log.get(commitIndex).getOp() == Raft.Operation.Put){
                    this.kvstore.put(log.get(commitIndex).getKey(), log.get(commitIndex).getValue());
                }else{
                    if (this.kvstore.containsKey(log.get(commitIndex).getKey())){
                        this.kvstore.remove(log.get(commitIndex).getKey());
                    }
                }

            }
            try {
                this.resetQueue.put(Param.REST_ELECTION_TIME_OUT);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Raft.AppendEntriesReply appendEntriesReply = Raft.AppendEntriesReply.newBuilder()
                    .setSuccess(rSuccess)
                    .setTerm(this.currentTerm)
                    .setFrom(this.nodeId)
                    .build();
            responseObserver.onNext(appendEntriesReply);
            responseObserver.onCompleted();


        }


    }

    @Override
    public void checkEvents(Raft.CheckEventsArgs request, StreamObserver<Raft.CheckEventsReply> responseObserver) {
        super.checkEvents(request, responseObserver);
    }

    @Override
    public void whoAreYou(Raft.WhoAreYouArgs request, StreamObserver<Raft.WhoAreYouReply> responseObserver) {
        Raft.WhoAreYouReply whoAreYouReply = Raft.WhoAreYouReply.newBuilder()
                .setMsg(localhost + ":" + lport + "| state" + this.serverState + "| votedFor" + this.votedFor).build();
        responseObserver.onNext(whoAreYouReply);
        responseObserver.onCompleted();
    }
}

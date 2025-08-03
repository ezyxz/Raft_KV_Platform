package com.cuhk.raft.bean;

import com.cuhk.raft.pb.Raft;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@AllArgsConstructor
@Getter
@Setter
public class RaftStateBean {
    private final int nodeId;

    private int currentTerm = 0;
    private int votedFor = -1;
    private Raft.Role currentRole;
    private int commitIndex = -1;

    private  int heartBeatInterval;  //作为leader时，心跳间隔时间 单位ms
    private  int electionTimeout; //作为candidate时，选举超市时间 单位ms

    private Map<Integer, Integer> termVoteMap;

    public RaftStateBean(int nodeId, Raft.Role currentRole, int heartBeatInterval, int electionTimeout) {
        this.nodeId = nodeId;
        this.currentRole = currentRole;
        this.heartBeatInterval = heartBeatInterval;
        this.electionTimeout = electionTimeout;
        this.termVoteMap = new ConcurrentHashMap<>();
    }
}

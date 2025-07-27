package com.cuhk.raft.core;

import com.cuhk.raft.bean.ReplicationConfig;

import java.io.IOException;

public class RaftProgramme {

    private final ReplicationConfig replicationConfig;
    private final RaftServer raftServer;
    private final int nodeId;
    private final int port;
    private final RaftCore raftCore;

    public RaftProgramme(ReplicationConfig replicationConfig) {
        this.replicationConfig = replicationConfig;
        this.nodeId = replicationConfig.getNodeId();
        this.port = Integer.parseInt(replicationConfig.getReplicatorMap().get(this.nodeId).getAddress().split(":")[1]);
        this.raftServer = new RaftServer(port);
        this.raftCore = new RaftCore(this.nodeId, 1000, 1000);
    }

    public void run(){

    }

    public void serverStart() throws IOException {
        this.raftServer.build(this.raftCore);
        this.raftServer.start();
    }
}

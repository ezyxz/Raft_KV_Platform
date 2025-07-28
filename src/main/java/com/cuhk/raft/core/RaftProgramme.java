package com.cuhk.raft.core;

import com.cuhk.raft.bean.ReplicationConfig;
import com.cuhk.raft.bean.ReplicatorBean;
import com.cuhk.raft.utils.SingleThreadExecutorManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class RaftProgramme {

    private final ReplicationConfig replicationConfig;
    private final RaftServerInr raftServer;
    private final int nodeId;
    private final int port;
    private final RaftCore raftCore;

    private List<RaftClientInr> raftClientInrList;

    public RaftProgramme(ReplicationConfig replicationConfig) {
        this.replicationConfig = replicationConfig;
        this.nodeId = replicationConfig.getNodeId();
        this.port = Integer.parseInt(replicationConfig.getReplicatorMap().get(this.nodeId).getAddress().split(":")[1]);
        this.raftServer = new RaftServerInr(port);
        this.raftCore = new RaftCore(this.nodeId, 1000, 1000);

    }

    public void run(){

        SingleThreadExecutorManager serverThread = new SingleThreadExecutorManager(
                ()->serverStart()
        );
        serverThread.start();

        int replications = replicationConfig.getReplications();
        raftClientInrList = new ArrayList<>(replications);
        Set<Map.Entry<Integer, ReplicatorBean>> entries = replicationConfig.getReplicatorMap().entrySet();
        for (Map.Entry<Integer, ReplicatorBean> entry : entries) {
            Integer key = entry.getKey();
            ReplicatorBean replicatorBean = entry.getValue();
            raftClientInrList.add(new RaftClientInr(replicatorBean));
        }

        while (true) {
            switch (this.raftCore.currentRole){
                case Follower:
                    break;
                case Candidate:
                    break;
                case Leader:
                    break;
            }
        }


    }

    public void serverStart()  {
        this.raftServer.build(this.raftCore);
        try {
            this.raftServer.start();
            this.raftServer.blockUntilShutdown();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }


    }
}

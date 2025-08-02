package com.cuhk.raft;

import com.cuhk.raft.bean.ReplicationConfig;
import com.cuhk.raft.core.RaftProgramme;
import com.cuhk.raft.utils.StringUtils;
import com.cuhk.raft.utils.ToolUtils;
import org.apache.log4j.Logger;

public class RaftBoot {

    private final static Logger logger = Logger.getLogger(RaftBoot.class);

    public static void main(String[] args) throws Exception {
        logger.info("Raft start...");
        logger.info("@author:" + StringUtils.AUTHOR);
        logger.info(StringUtils.RAFT);
        logger.info("Loading configuration from /conf/raft-core.xml and args...");
        ReplicationConfig replicationConfig = ToolUtils.parseReplicationConfig(args);
        logger.info("replicationConfig Load below:");
        logger.info(replicationConfig);
        RaftProgramme raftProgramme = new RaftProgramme(replicationConfig);
        raftProgramme.run();
    }



}

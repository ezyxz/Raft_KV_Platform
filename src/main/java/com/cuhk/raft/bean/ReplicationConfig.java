package com.cuhk.raft.bean;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;
import java.util.StringJoiner;

@AllArgsConstructor
@Getter
public class ReplicationConfig {

    private int replications; // replica数量

    private int nodeId;  // 对应XML中的local_replicator_id 本机id

    private Map<Integer, ReplicatorBean> replicatorMap;  // nodeId对应ip

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("ReplicationConfig {").append("\n");
        builder.append("  replications=").append(replications).append(",\n");
        builder.append("  nodeId=").append(nodeId).append(",\n");
        builder.append("  replicatorMap=").append("{").append("\n");

        for (Map.Entry<Integer, ReplicatorBean> entry : replicatorMap.entrySet()) {
            builder.append("    ").append(entry.getKey()).append("=")
                    .append(entry.getValue().toString()).append(",\n");
        }

        // 去除最后一个逗号和换行符
        if (!replicatorMap.isEmpty()) {
            builder.setLength(builder.length() - 2);  // 删除最后的 ",\n"
        }

        builder.append("\n  }").append("\n");
        builder.append("}");

        return builder.toString();
    }
}

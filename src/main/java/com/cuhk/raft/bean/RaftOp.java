package com.cuhk.raft.bean;

/**
 * Raft 操作类型枚举
 */
public enum RaftOp {
    /** 写入/更新操作 */
    PUT,

    /** 删除操作 */
    DELETE
}

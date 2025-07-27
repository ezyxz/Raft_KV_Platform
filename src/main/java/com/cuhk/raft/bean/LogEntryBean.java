package com.cuhk.raft.bean;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

public class LogEntryBean {
    private  long index;       // 日志索引
    private  long term;        // 任期号
    private  RaftOp op;        // 操作类型（PUT/DELETE）
    private  byte[] key;       // 键
    private  byte[] value;     // 值（DELETE操作时为null）
}

package com.cuhk.raft.test;

import com.cuhk.raft.bean.LogEntryBean;
import com.cuhk.raft.bean.RaftOp;
import com.cuhk.raft.persistence.FilePersistence;
import com.cuhk.raft.persistence.PersistenceStrategy;
import com.cuhk.raft.persistence.RocksDBPersistence;

import java.util.List;
import java.util.Properties;

public class TestRocksDBPersistence {
    public static void main(String[] args) {
        // 初始化
        Properties props = new Properties();
        props.setProperty("db.path", "/tmp/raft-db");
        PersistenceStrategy persistence = new FilePersistence();
        persistence.persistenceInit(props);

// 添加条目
        LogEntryBean entry1 = new LogEntryBean(1, 1, RaftOp.PUT, "key1".getBytes(), "value1".getBytes());
        LogEntryBean entry2 = new LogEntryBean(2, 1, RaftOp.PUT, "key2".getBytes(), "value2".getBytes());
        persistence.save(entry1);
        persistence.save(entry2);

// 查询使用
        System.out.println("Total entries: " + persistence.getEntryCount());

        List<LogEntryBean> allEntries = persistence.getAll();
        allEntries.forEach(entry ->
                System.out.println("Index: " + entry.getIndex() + ", Key: " + new String(entry.getKey()))
        );

// 关闭
        persistence.close();
    }
}

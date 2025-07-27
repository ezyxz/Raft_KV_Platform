package com.cuhk.raft.persistence;

import com.cuhk.raft.bean.LogEntryBean;

import java.util.Properties;
import java.util.List;

public interface PersistenceStrategy {

    public int persistenceInit(Properties props);

    public int save(LogEntryBean logEntryBean);

    public LogEntryBean get(long index);

    public List<LogEntryBean> getAll();  // 新增：获取所有条目

    public int getEntryCount();         // 新增：获取条目数量

    public int delete(LogEntryBean logEntryBean);

    public int close();





}

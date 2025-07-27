package com.cuhk.raft.persistence;

import com.cuhk.raft.bean.LogEntryBean;
import com.cuhk.raft.bean.RaftOp;
import org.rocksdb.*;
import org.rocksdb.RocksIterator;

import java.io.File;
import java.nio.ByteBuffer;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class RocksDBPersistence implements PersistenceStrategy {
    private RocksDB db;
    private final AtomicInteger entryCount = new AtomicInteger(0);
    private static final byte[] COUNT_KEY = "ENTRY_COUNT".getBytes();

    @Override
    public int persistenceInit(Properties props) {
        try {
            RocksDB.loadLibrary();
            String dbPath = props.getProperty("db.path", "/tmp/raft-rocksdb");

            Options options = new Options()
                    .setCreateIfMissing(true)
                    .setMaxOpenFiles(-1);

            // 确保目录存在
            File dbDir = new File(dbPath);
            if (!dbDir.exists()) {
                if (!dbDir.mkdirs()) {
                    throw new RuntimeException("Failed to create DB directory: " + dbPath);
                }
            }

            this.db = RocksDB.open(options, dbPath);

            // 初始化条目计数器
            byte[] countBytes = db.get(COUNT_KEY);
            if (countBytes != null) {
                entryCount.set(ByteBuffer.wrap(countBytes).getInt());
            }
            return 0;
        } catch (RocksDBException e) {
            e.printStackTrace();
            return -1;
        }
    }

    @Override
    public int save(LogEntryBean logEntry) {
        try {
            byte[] key = serializeKey(logEntry.getIndex());
            byte[] value = serializeLogEntry(logEntry);

            // 如果是新条目（根据index判断是否已存在）
            if (db.get(key) == null) {
                entryCount.incrementAndGet();
                db.put(COUNT_KEY, ByteBuffer.allocate(4).putInt(entryCount.get()).array());
            }

            db.put(key, value);
            return 0;
        } catch (RocksDBException e) {
            e.printStackTrace();
            return -1;
        }
    }

    @Override
    public LogEntryBean get(long index) {
        try {
            byte[] key = serializeKey(index);
            byte[] value = db.get(key);
            return value != null ? deserializeLogEntry(index, value) : null;
        } catch (RocksDBException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<LogEntryBean> getAll() {
        List<LogEntryBean> entries = new ArrayList<>();
        try (RocksIterator iterator = db.newIterator()) {
            for (iterator.seekToFirst(); iterator.isValid(); iterator.next()) {
                // 跳过计数器的key
                if (Arrays.equals(iterator.key(), COUNT_KEY)) {
                    continue;
                }

                long index = ByteBuffer.wrap(iterator.key()).getLong();
                entries.add(deserializeLogEntry(index, iterator.value()));
            }
            return entries;
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    @Override
    public int getEntryCount() {
        return entryCount.get();
    }

    @Override
    public int delete(LogEntryBean logEntry) {
        try {
            byte[] key = serializeKey(logEntry.getIndex());

            // 如果key存在才减少计数
            if (db.get(key) != null) {
                entryCount.decrementAndGet();
                db.put(COUNT_KEY, ByteBuffer.allocate(4).putInt(entryCount.get()).array());
            }

            db.delete(key);
            return 0;
        } catch (RocksDBException e) {
            e.printStackTrace();
            return -1;
        }
    }

    @Override
    public int close() {
        if (db != null) {
            db.close();
            return 0;
        }
        return -1;
    }

    // 序列化方法（保持不变）
    private byte[] serializeKey(long index) {
        ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
        buffer.putLong(index);
        return buffer.array();
    }

    private byte[] serializeLogEntry(LogEntryBean entry) {
        try {
            ByteBuffer buffer = ByteBuffer.allocate(
                    Long.BYTES * 2 + 1 + 4 +
                            (entry.getKey() != null ? entry.getKey().length : 0) +
                            4 + (entry.getValue() != null ? entry.getValue().length : 0)
            );

            buffer.putLong(entry.getIndex());
            buffer.putLong(entry.getTerm());
            buffer.put((byte)(entry.getOp() == RaftOp.PUT ? 1 : 0));

            if (entry.getKey() != null) {
                buffer.putInt(entry.getKey().length);
                buffer.put(entry.getKey());
            } else {
                buffer.putInt(0);
            }

            if (entry.getValue() != null) {
                buffer.putInt(entry.getValue().length);
                buffer.put(entry.getValue());
            } else {
                buffer.putInt(0);
            }

            return buffer.array();
        } catch (Exception e) {
            throw new RuntimeException("Serialization failed", e);
        }
    }

    // 反序列化方法
    private LogEntryBean deserializeLogEntry(long index, byte[] data) {
        try {
            ByteBuffer buffer = ByteBuffer.wrap(data);

            LogEntryBean entry = new LogEntryBean();
            entry.setIndex(buffer.getLong());
            entry.setTerm(buffer.getLong());

            byte opByte = buffer.get();
            entry.setOp(opByte == 1 ? RaftOp.PUT : RaftOp.DELETE);

            int keyLen = buffer.getInt();
            if (keyLen > 0) {
                byte[] key = new byte[keyLen];
                buffer.get(key);
                entry.setKey(key);
            }

            int valueLen = buffer.getInt();
            if (valueLen > 0) {
                byte[] value = new byte[valueLen];
                buffer.get(value);
                entry.setValue(value);
            }

            return entry;
        } catch (Exception e) {
            throw new RuntimeException("Deserialization failed", e);
        }
    }
}
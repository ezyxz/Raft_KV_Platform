package com.cuhk.raft.persistence;

import com.cuhk.raft.bean.LogEntryBean;
import com.cuhk.raft.bean.RaftOp;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class FilePersistence implements PersistenceStrategy {
    private Path dbDir;
    private final AtomicInteger entryCount = new AtomicInteger(0);
    private static final String COUNT_FILE = "entry.count";
    private static final String FILE_PREFIX = "logentry_";
    private static final String FILE_SUFFIX = ".bin";

    @Override
    public int persistenceInit(Properties props) {
        try {
            String dbPath = props.getProperty("db.path", System.getProperty("java.io.tmpdir") + "/raft-file-db");
            this.dbDir = Paths.get(dbPath);

            // 创建目录（如果不存在）
            Files.createDirectories(dbDir);

            // 加载条目计数
            Path countFile = dbDir.resolve(COUNT_FILE);
            if (Files.exists(countFile)) {
                byte[] countBytes = Files.readAllBytes(countFile);
                entryCount.set(ByteBuffer.wrap(countBytes).getInt());
            }
            return 0;
        } catch (IOException e) {
            throw new RuntimeException("File persistence init failed", e);
        }
    }

    @Override
    public int save(LogEntryBean logEntry) {
        Path entryFile = getEntryFilePath(logEntry.getIndex());
        try {
            // 如果是新条目才增加计数
            if (!Files.exists(entryFile)) {
                entryCount.incrementAndGet();
                updateCountFile();
            }

            // 使用NIO高效写入
            try (FileChannel channel = FileChannel.open(entryFile,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.WRITE,
                    StandardOpenOption.TRUNCATE_EXISTING)) {

                channel.write(ByteBuffer.wrap(serializeLogEntry(logEntry)));
            }
            return 0;
        } catch (IOException e) {
            throw new RuntimeException("Failed to save log entry", e);
        }
    }

    @Override
    public LogEntryBean get(long index) {
        Path entryFile = getEntryFilePath(index);
        if (!Files.exists(entryFile)) {
            return null;
        }

        try {
            byte[] data = Files.readAllBytes(entryFile);
            return deserializeLogEntry(index, data);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read log entry", e);
        }
    }

    @Override
    public List<LogEntryBean> getAll() {
        List<LogEntryBean> entries = new ArrayList<>();
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(dbDir, FILE_PREFIX + "*" + FILE_SUFFIX)) {
            for (Path entryFile : stream) {
                try {
                    long index = extractIndexFromFilename(entryFile);
                    byte[] data = Files.readAllBytes(entryFile);
                    entries.add(deserializeLogEntry(index, data));
                } catch (Exception e) {
                    System.err.println("Failed to read file: " + entryFile);
                }
            }
            // 按索引排序
            entries.sort(Comparator.comparingLong(LogEntryBean::getIndex));
            return entries;
        } catch (IOException e) {
            throw new RuntimeException("Failed to list log entries", e);
        }
    }

    @Override
    public int getEntryCount() {
        return entryCount.get();
    }

    @Override
    public int delete(LogEntryBean logEntry) {
        Path entryFile = getEntryFilePath(logEntry.getIndex());
        try {
            if (Files.exists(entryFile)) {
                Files.delete(entryFile);
                entryCount.decrementAndGet();
                updateCountFile();
            }
            return 0;
        } catch (IOException e) {
            throw new RuntimeException("Failed to delete log entry", e);
        }
    }

    @Override
    public int close() {
        // 文件系统实现不需要特殊关闭操作
        return 0;
    }

    // ============== 辅助方法 ==============
    private Path getEntryFilePath(long index) {
        return dbDir.resolve(FILE_PREFIX + index + FILE_SUFFIX);
    }

    private long extractIndexFromFilename(Path file) {
        String filename = file.getFileName().toString();
        return Long.parseLong(filename
                .replace(FILE_PREFIX, "")
                .replace(FILE_SUFFIX, ""));
    }

    private void updateCountFile() throws IOException {
        Files.write(dbDir.resolve(COUNT_FILE),
                ByteBuffer.allocate(4).putInt(entryCount.get()).array(),
                StandardOpenOption.CREATE,
                StandardOpenOption.WRITE,
                StandardOpenOption.TRUNCATE_EXISTING);
    }

    // 序列化方法（与RocksDB实现相同）
    private byte[] serializeLogEntry(LogEntryBean entry) {
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
    }

    // 反序列化方法
    private LogEntryBean deserializeLogEntry(long index, byte[] data) {
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
    }
}
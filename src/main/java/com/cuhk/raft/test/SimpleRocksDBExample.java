package com.cuhk.raft.test;

import org.rocksdb.Options;
import org.rocksdb.RocksDB;
import org.rocksdb.RocksDBException;

public class SimpleRocksDBExample {

    static {
        // 加载 RocksDB 库
        RocksDB.loadLibrary();
    }

    public static void main(String[] args) {
        // 数据库路径
        String dbPath = "/tmp/rocksdb_simple_example";

        // 1. 打开数据库
        try (Options options = new Options().setCreateIfMissing(true);
             RocksDB db = RocksDB.open(options, dbPath)) {

            System.out.println("RocksDB opened successfully");

            // 2. 写入数据
            byte[] key1 = "key1".getBytes();
            byte[] value1 = "value1".getBytes();
            db.put(key1, value1);

            byte[] key2 = "key2".getBytes();
            byte[] value2 = "value2".getBytes();
            db.put(key2, value2);

            System.out.println("Data written to RocksDB");

            // 3. 读取数据
            byte[] readValue1 = db.get(key1);
            byte[] readValue2 = db.get(key2);

            System.out.println("key1: " + new String(readValue1));
            System.out.println("key2: " + new String(readValue2));

            // 4. 删除数据
            db.delete(key1);
            System.out.println("key1 deleted");

            // 检查是否删除成功
            byte[] deletedValue = db.get(key1);
            System.out.println("key1 after deletion: " + (deletedValue == null ? "null" : new String(deletedValue)));

        } catch (RocksDBException e) {
            System.err.println("RocksDB operation failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
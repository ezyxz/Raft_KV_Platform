package com.cuhk.raft.test;

import com.cuhk.raft.utils.SingleThreadExecutorManager;

public class SingleThreadExecutorManagerTest {
    public static void main(String[] args) {
        // 创建你的任务
        Runnable myTask = () -> {
            System.out.println("Working... " + System.currentTimeMillis());
            // 模拟随机失败
            if (Math.random() > 0.9) {
                throw new RuntimeException("Random failure");
            }
            try {
                Thread.sleep(500); // 模拟工作
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        };
        // 创建并启动单线程执行器
        SingleThreadExecutorManager manager = new SingleThreadExecutorManager(myTask);
        manager.start();

        // 主线程等待一段时间后停止
        try {
            Thread.sleep(30000); // 运行30秒
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        manager.shutdown();
        System.out.println("Stopped persistent thread");
    }
}

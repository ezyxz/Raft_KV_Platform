package com.cuhk.raft.utils;

import com.cuhk.raft.RaftBoot;
import org.apache.log4j.Logger;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicBoolean;

public class SingleThreadExecutorManager {

    private final static Logger logger = Logger.getLogger(SingleThreadExecutorManager.class);

    private final ExecutorService executor;
    private final AtomicBoolean running = new AtomicBoolean(true);
    private final Runnable task;

    public SingleThreadExecutorManager(Runnable task) {
        this.task = task;
        this.executor = Executors.newFixedThreadPool(1, new ResilientThreadFactory());
    }

    public void start() {
        executor.execute(this::runTask);
    }

    private void runTask() {
        while (running.get()) {
            try {
                task.run();
            } catch (Throwable t) {
                logger.error("Task failed, restarting...");
                logError(t);
                sleepSafely(1000); // 错误后暂停1秒
            }
        }
    }

    public void shutdown() {
        running.set(false);
        executor.shutdownNow(); // 立即关闭，因为我们有自己的运行状态控制
    }

    private void sleepSafely(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void logError(Throwable t) {
        // 实现你的日志记录逻辑
        t.printStackTrace();
    }

    private static class ResilientThreadFactory implements ThreadFactory {
        @Override
        public Thread newThread(Runnable r) {
            Thread thread = new Thread(r, "persistent-single-worker");
            thread.setUncaughtExceptionHandler((t, e) -> {
                logger.error("Thread crashed: " + e.getMessage());
                // 不需要处理，因为线程池会创建新线程
            });
            return thread;
        }
    }
}
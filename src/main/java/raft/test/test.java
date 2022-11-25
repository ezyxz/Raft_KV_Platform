package raft.test;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

public class test {
    public static void main(String[] args) throws InterruptedException {
        BlockingQueue<Integer> bq =new LinkedBlockingDeque<>();
        new Thread(()->{
            try {
                Thread.sleep(3000);
                bq.put(99);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
        while(true){
            Integer poll = bq.poll(5000, TimeUnit.MILLISECONDS);
            System.out.println("reset \n" + poll);
        }
    }
}

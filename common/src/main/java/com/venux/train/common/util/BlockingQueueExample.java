package com.venux.train.common.util;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class BlockingQueueExample {

    // 创建一个BlockingQueue，容量为10
    private static final BlockingQueue<Integer> queue = new ArrayBlockingQueue<>(10);

    // 生产者线程
    static class Producer implements Runnable {
        public void run() {
            int value = 0;
            while (true) {
                try {
                    // 生产数据
                    Thread.sleep(1000); // 模拟耗时操作
                    value++;
                    System.out.println("生产者生产了：" + value);

                    // 将生产的数据放入队列
                    queue.put(value);

                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    // 消费者线程
    static class Consumer implements Runnable {
        public void run() {
            while (true) {
                try {
                    // 从队列中取出数据
                    Integer value = queue.take();
                    System.out.println("消费者消费了：" + value);

                    // 假设消费者处理数据也需要一些时间
                    Thread.sleep(200);

                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    public static void main(String[] args) {
        // 创建并启动生产者线程
        Thread producerThread = new Thread(new Producer());
        producerThread.start();

        // 创建并启动消费者线程
        Thread consumerThread = new Thread(new Consumer());
        consumerThread.start();
    }
}
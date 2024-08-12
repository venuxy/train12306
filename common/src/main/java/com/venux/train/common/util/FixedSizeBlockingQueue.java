package com.venux.train.common.util;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 一个具有固定大小的阻塞队列，支持线程安全的操作。
 * 它允许在不发生死锁的情况下添加和移除元素。
 *
 * @param <T> 队列中元素的类型
 */
public class FixedSizeBlockingQueue<T> {
    private final int capacity;// 队列容量
    private final Queue<T> queue;// 底层的队列
    private final ReentrantLock lock;// 用于同步的锁
    private final Condition notFull;// 队列不满的条件变量，用于阻塞等待队列不满，以便添加元素
    private final Condition notEmpty;// 队列不空的条件变量，用于阻塞等待队列不空，以便移除元素
    private volatile boolean isClosed = false; // 用于标记队列是否已关闭

    // 构造函数，初始化队列的容量，以及相关的锁和条件变量
    public FixedSizeBlockingQueue(int capacity) {
        this.capacity = capacity;
        this.queue = new LinkedList<>();
        this.lock = new ReentrantLock();
        this.notFull = lock.newCondition();
        this.notEmpty = lock.newCondition();
    }
    // 将元素放入队列，如果队列已满，则阻塞当前线程，直到队列不满或者队列被关闭
    public void put(T item) throws InterruptedException {
        lock.lock();
        try {
            while (queue.size() == capacity || isClosed) { // 检查队列是否已满或已关闭
                if (isClosed) {
                    throw new IllegalStateException("Queue is closed");
                }
                notFull.await();
            }
            queue.add(item);
            notEmpty.signal();
        } finally {
            lock.unlock();
        }
    }
    // 从队列中取出一个元素，如果队列为空，则阻塞当前线程，直到队列不空或者队列被关闭
    public T take() throws InterruptedException {
        lock.lock();
        try {
            while (queue.isEmpty()) {
                notEmpty.await();
            }
            T item = queue.poll();
            notFull.signal();
            return item;
        } finally {
            lock.unlock();
        }
    }
    // 返回队列中当前元素的数量
    public int size() {
        lock.lock();
        try {
            return queue.size();
        } finally {
            lock.unlock();
        }
    }
    // 关闭队列，不再接受新的元素
    public void close() {
        lock.lock();
        try {
            isClosed = true; // 设置关闭标志
            // 可选：唤醒所有等待的put线程
            notFull.signalAll();
        } finally {
            lock.unlock();
        }
    }
    public static void main(String[] args) {
        FixedSizeBlockingQueue<Integer> queue = new FixedSizeBlockingQueue<>(5);
        Thread producer = new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                try {
                    queue.put(i);
                    System.out.println("Produced: " + i);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        Thread consumer = new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                try {
                    int item = queue.take();
                    System.out.println("Consumed: " + item);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        producer.start();
        consumer.start();
        try {
            producer.join();
            consumer.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Queue size: " + queue.size());
        queue.close();
        System.out.println("Queue closed.");
    }
}
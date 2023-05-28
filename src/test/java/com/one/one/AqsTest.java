package com.one.one;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.AbstractQueuedLongSynchronizer;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * @author XuLongjie
 * @create 2022-11-21-20:56
 */
@Slf4j(topic = "c.Aqs")
public class AqsTest {
    public static void main(String[] args) {
        MylLock lock=new MylLock();;
        new Thread(() -> {
            lock.lock();
            try {
                log.debug("locking....");
            } finally {
                lock.unlock();
                log.debug("unlocking");
            }
        },"t1").start();
        new Thread(() -> {
            lock.lock();
            try {
                log.debug("locking....");
            } finally {
                lock.unlock();
                log.debug("unlocking");
            }
        },"t2").start();
    }
}
//自定义锁，独占锁
class MylLock implements Lock{
    //同步器类
    // 独占锁  同步器类
    class MySync extends AbstractQueuedSynchronizer {
        @Override
        protected boolean tryAcquire(int arg) {
            if(compareAndSetState(0, 1)) {
                // 加上了锁，并设置 owner 为当前线程
                setExclusiveOwnerThread(Thread.currentThread());
                return true;
            }
            return false;
        }

        @Override
        protected boolean tryRelease(int arg) {
            setExclusiveOwnerThread(null);
            setState(0);
            return true;
        }

        @Override // 是否持有独占锁
        protected boolean isHeldExclusively() {
            return getState() == 1;
        }

        public Condition newCondition() {
            return new AbstractQueuedSynchronizer.ConditionObject();
        }
    }


    private MySync sync=new MySync();

    @Override//加锁
    public void lock() {
        sync.acquire(1);
    }

    @Override//可打断锁
    public void lockInterruptibly() throws InterruptedException {
        sync.acquireInterruptibly(1);
    }

    @Override//尝试加锁
    public boolean tryLock() {

        return sync.tryAcquire(1);
    }

    @Override//带时间的尝试锁
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return sync.tryAcquireNanos(1,unit.toNanos(time));
    }

    @Override//解锁
    public void unlock() {
        sync.release(1);
    }

    @Override//创建条件变量
    public Condition newCondition() {
        return sync.newCondition();
    }
}
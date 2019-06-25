package com.yxhe.lock.lock;

import java.io.Closeable;

/**
 * 分布式锁
 */
public interface DistributedLock extends Closeable {
    String LOCK_PATH_PREFIX = "distributed-lock";
    long DEFAULT_WAIT_TIMEOUT = 10L;
    long DEFAULT_RELEASE_TIMEOUT = 30L;

    /**
     * 获取锁
     */
    <T> T lock(Mutex<T> mutex);

    /**
     * 获取锁
     *
     * @param waitTime 等待时间
     */
    <T> T lock(long waitTime, Mutex<T> mutex);

    /**
     * 获取锁
     *
     * @param waitTime    等待时间
     * @param releaseTime 锁释放时间
     */
    <T> T lock(long waitTime, long releaseTime, Mutex<T> mutex);

}
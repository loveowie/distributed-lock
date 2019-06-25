package com.yxhe.lock.lock;

/**
 * @author yxhe
 * @since 2018-12-21 10:53
 */
public interface Mutex<T> {

    /**
     * 锁
     */
    String lockPath();

    /**
     * 执行锁
     */
    T execute();

}

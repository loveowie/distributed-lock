package com.yxhe.lock.lock;

/**
 * @author yxhe
 * @since 2018-12-21 10:53
 */
public abstract class AbstractMutex<T> implements Mutex<T> {
    private final String lockPath;

    public AbstractMutex(String lockPath) {
        this.lockPath = lockPath;
    }

    @Override
    public String lockPath() {
        return this.lockPath;
    }

}

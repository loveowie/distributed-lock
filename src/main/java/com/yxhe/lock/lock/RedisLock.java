package com.yxhe.lock.lock;

import com.yxhe.lock.exception.LockException;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * @author yxhe
 * @since 2018-12-21 10:53
 */
@Slf4j
public class RedisLock implements DistributedLock {
    private final RedissonClient client;

    public RedisLock(RedissonClient client) {
        this.client = client;
    }

    @Override
    public <T> T lock(Mutex<T> mutex) {
        return lock(DEFAULT_WAIT_TIMEOUT, DEFAULT_RELEASE_TIMEOUT, mutex);
    }

    @Override
    public <T> T lock(long waitTime, Mutex<T> mutex) {
        return lock(waitTime, DEFAULT_RELEASE_TIMEOUT, mutex);
    }

    @Override
    public <T> T lock(long waitTime, long releaseTime, Mutex<T> mutex) {
        final String lockPath = LOCK_PATH_PREFIX + ":" + mutex.lockPath();
        RLock lock = client.getLock(lockPath);
        boolean locked = false;
        try {
            try {
                locked = lock.tryLock(waitTime, releaseTime, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                throw new LockException(String.format("Lock[path=%s] failed", lockPath));
            }
            if (!locked) {
                throw new LockException(String.format("Lock[path=%s] failed", lockPath));
            }
            return mutex.execute();
        } finally {
            if (locked) {
                try {
                    lock.unlock();
                } catch (Exception e) {
                    log.error(String.format("An error happened when try to release lock[path=%s], call method forceUnlock", lockPath), e);
                    lock.forceUnlock();
                }
            }
        }
    }

    @Override
    public void close() throws IOException {
        if (!client.isShutdown() && !client.isShuttingDown()) {
            client.shutdown();
        }
    }
}

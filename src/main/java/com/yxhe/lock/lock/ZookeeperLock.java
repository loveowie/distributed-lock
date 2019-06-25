package com.yxhe.lock.lock;

import com.google.common.collect.Maps;
import com.yxhe.lock.exception.LockException;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.imps.CuratorFrameworkState;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;

import java.io.IOException;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

/**
 * @author yxhe
 * @since 2018-11-23 9:40
 */
@Slf4j
public class ZookeeperLock implements DistributedLock {

    private final CuratorFramework client;

    public ZookeeperLock(CuratorFramework client) {
        this.client = client;
    }

    private final ConcurrentMap<String, InterProcessMutex> lockData = Maps.newConcurrentMap();

    @Override
    public <T> T lock(Mutex<T> mutex) {
        return this.lock(DEFAULT_WAIT_TIMEOUT, DEFAULT_RELEASE_TIMEOUT,mutex);
    }

    @Override
    public <T> T lock(long waitTime, Mutex<T> mutex) {
        return this.lock(waitTime, DEFAULT_RELEASE_TIMEOUT, mutex);
    }

    @Override
    public <T> T lock(long waitTime, long releaseTime, Mutex<T> mutex) {

        final String lockPath = "/" + LOCK_PATH_PREFIX + "/" + mutex.lockPath();

        InterProcessMutex lock = lockData.get(lockPath);
        if (lock == null){
            lock = new InterProcessMutex(client, lockPath);
            lockData.put(lockPath, lock);
        }

        try {
            if (lock.acquire(waitTime, TimeUnit.MILLISECONDS)) {
                return mutex.execute();
            }
            else {
                throw new LockException(String.format("Lock[path=%s] failed", lockPath));
            }
        } catch (Exception e) {
            throw new LockException(String.format("Lock[path=%s] failed", lockPath));
        }
        finally {
            try {
                if (lock.isOwnedByCurrentThread()) {
                    lock.release();
                }
            } catch (Exception e) {
                log.error(String.format("An error happened when try to release lock[path=%s]", lockPath), e);
            }
        }
    }

    @Override
    public void close() throws IOException {
        if (client.getState() != CuratorFrameworkState.STOPPED) {
            client.close();
        }
    }
}

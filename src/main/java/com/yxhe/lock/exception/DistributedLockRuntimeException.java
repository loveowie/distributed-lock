package com.yxhe.lock.exception;

/**
 * @author yxhe
 * @since 2018-12-21 10:53
 */
public class DistributedLockRuntimeException extends RuntimeException {

    public DistributedLockRuntimeException(String message) {
        super(message);
    }

    public DistributedLockRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }
}

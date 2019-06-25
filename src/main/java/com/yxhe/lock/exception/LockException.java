package com.yxhe.lock.exception;

/**
 * @author yxhe
 * @since 2018-12-21 10:53
 */
public class LockException extends RuntimeException {

    public LockException(String message) {
        super(message);
    }

    public LockException(Throwable cause) {
        super(cause);
    }

    public LockException(String message, Throwable cause) {
        super(message, cause);
    }
}

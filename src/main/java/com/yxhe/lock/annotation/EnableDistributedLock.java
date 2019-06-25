package com.yxhe.lock.annotation;

import com.yxhe.lock.config.RedisLockAutoConfiguration;
import com.yxhe.lock.enums.LockType;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author yxhe
 * @since 2018-12-21 10:55
 */
@Inherited
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Import(RedisLockAutoConfiguration.class)
public @interface EnableDistributedLock {

    LockType lockType() default LockType.REDIS;
}

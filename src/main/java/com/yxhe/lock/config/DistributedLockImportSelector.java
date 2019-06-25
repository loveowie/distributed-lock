package com.yxhe.lock.config;

import com.yxhe.lock.annotation.LockTypeImportSelector;
import com.yxhe.lock.enums.LockType;

/**
 * @author yxhe
 * @since 2018-12-21 10:53
 */
public class DistributedLockImportSelector extends LockTypeImportSelector {

    @Override
    protected String[] selectImports(LockType lockType) {
        switch(lockType) {
            case REDIS:
                return new String[]{"RedisLockAutoConfiguration"};
            case ZOOKEEPER:
                return new String[]{"ZookeeperLockAutoConfiguration"};
            default:
                return null;
        }
    }
}

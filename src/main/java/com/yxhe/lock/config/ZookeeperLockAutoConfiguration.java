package com.yxhe.lock.config;

import com.yxhe.lock.lock.DistributedLock;
import com.yxhe.lock.lock.ZookeeperLock;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;

/**
 * zk锁服务自动配置
 *
 * @author yxhe4
 * @since 2018/11/28 14:23
 */
@ConditionalOnClass(DistributedLock.class)
@AutoConfigureAfter(DistributedLockProperties.class)
public class ZookeeperLockAutoConfiguration {

    private final DistributedLockProperties properties;

    public ZookeeperLockAutoConfiguration(DistributedLockProperties properties) {
        this.properties = properties;
    }

    @Bean(name = "zookeeperLock", destroyMethod = "close")
    public DistributedLock zookeeperLock() {
        return new ZookeeperLock(curator());
    }

    private CuratorFramework curator() {
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        CuratorFramework client = CuratorFrameworkFactory.newClient(properties.getZookeeper().getConfigCenterHost(), retryPolicy);
        client.start();
        return client;
    }
}

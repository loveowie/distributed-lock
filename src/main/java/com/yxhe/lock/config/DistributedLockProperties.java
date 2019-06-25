package com.yxhe.lock.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author heyx
 * @since 2019/6/16 20:53
 */
@Data
@ConfigurationProperties(prefix = "lock.config")
public class DistributedLockProperties {

    private RedisProperties redis;
    private ZookeeperProperties zookeeper;

    @Data
    public static class RedisProperties {
        private boolean cluster;
        private String host;
        private String port;
        private String password;
        private String database;
    }

    @Data
    public static class ZookeeperProperties {
        private String configCenterHost;
    }
}

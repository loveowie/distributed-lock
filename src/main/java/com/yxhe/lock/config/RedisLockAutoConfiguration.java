package com.yxhe.lock.config;

import com.yxhe.lock.lock.DistributedLock;
import com.yxhe.lock.lock.RedisLock;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.config.SingleServerConfig;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.util.StringUtils;

/**
 * redis锁服务自动配置
 *
 * @author heyx
 * @since 2019/6/16 20:53
 */
@ConditionalOnClass(DistributedLock.class)
@AutoConfigureAfter(DistributedLockProperties.class)
public class RedisLockAutoConfiguration {
    private boolean cluster;
    private String host;
    private int port;
    private String password;
    private int database;

    private final DistributedLockProperties properties;

    public RedisLockAutoConfiguration(DistributedLockProperties properties) {
        this.properties = properties;
    }

    @Bean(name = "redisLock", destroyMethod = "close")
    public DistributedLock redisLock() {
        return new RedisLock(redisson());
    }

    public RedissonClient redisson() {
        loadRedisConfig();
        Config config = new Config();
        if (cluster) {
            String[] preHosts = host.split(",");
            String[] hosts = new String[preHosts.length];
            for (int i = 0; i < preHosts.length; i++) {
                if (!"http://".startsWith(preHosts[i])) {
                    hosts[i] = "http://" + preHosts[i];
                }
            }
            config.useClusterServers()
                    .addNodeAddress(hosts)
                    .setPassword(password);
        } else {
            SingleServerConfig singleServerConfig = config.useSingleServer();
            singleServerConfig.setAddress("http://" + host + ":" + port)
                    .setDatabase(database);
            if (!StringUtils.isEmpty(password)) {
                singleServerConfig.setPassword(password);
            }
        }
        return Redisson.create(config);
    }

    private void loadRedisConfig() {
        DistributedLockProperties.RedisProperties redis = properties.getRedis();
        cluster = redis.isCluster();
        host = redis.getHost();
        port = StringUtils.isEmpty(redis.getPort()) ? 6379 : Integer.parseInt(redis.getPort());
        password = redis.getPassword();
        database = StringUtils.isEmpty(redis.getDatabase()) ? 0 : Integer.parseInt(redis.getDatabase());
    }
}

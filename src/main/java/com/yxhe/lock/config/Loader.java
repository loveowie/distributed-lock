package com.yxhe.lock.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author heyx
 * @since 2019/6/17 21:19
 */
@Component
@EnableConfigurationProperties(DistributedLockProperties.class)
public class Loader {
}

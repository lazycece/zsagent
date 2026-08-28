package com.lazycece.zsagent.infra.acl.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author lazycece
 * @date 2026/8/14
 */
@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "zsagent.storage")
public class StorageConfig {

    private String localFileBaseDir = "./storage/files/";

}

package com.VinoHouse.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "vino-house.alioss")
// Spring Boot 的配置属性（@ConfigurationProperties）要求使用 kebab-case（短横线分隔的小写字母）
@Data
public class AliOssProperties {

    private String endpoint;
    private String accessKeyId;
    private String accessKeySecret;
    private String bucketName;

}

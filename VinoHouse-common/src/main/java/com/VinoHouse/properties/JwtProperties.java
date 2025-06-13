package com.VinoHouse.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "vino-house.jwt")
// Spring Boot 的配置属性（@ConfigurationProperties）要求使用 kebab-case（短横线分隔的小写字母）
@Data
public class JwtProperties {

    /**
     * 管理端员工生成 jwt 令牌相关配置
     */
    private String adminSecretKey;
    private long adminTtl;  // 过期时间
    private String adminTokenName;

    /**
     * 用户端微信用户生成 jwt 令牌相关配置
     */
    private String userSecretKey;
    private long userTtl;
    private String userTokenName;

}

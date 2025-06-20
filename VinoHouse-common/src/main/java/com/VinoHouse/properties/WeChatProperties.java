package com.VinoHouse.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "vino-house.wechat")
// Spring Boot 的配置属性（@ConfigurationProperties）要求使用 kebab-case（短横线分隔的小写字母）
@Data
public class WeChatProperties {

    private String appid; // 小程序的 appid
    private String secret; // 小程序的秘钥
    private String mchid; // 商户号
    private String mchSerialNo; // 商户 API 证书的证书序列号
    private String privateKeyFilePath; // 商户私钥文件
    private String apiV3Key; // 证书解密的密钥
    private String weChatPayCertFilePath; // 平台证书
    private String notifyUrl; // 支付成功的回调地址
    private String refundNotifyUrl; // 退款成功的回调地址

}

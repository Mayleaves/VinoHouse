package com.VinoHouse.config;

import com.VinoHouse.interceptor.JwtTokenAdminInterceptor;
import com.VinoHouse.interceptor.JwtTokenUserInterceptor;
import com.VinoHouse.json.JacksonObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.List;

/**
 * 配置类，注册 web 层相关组件
 */
@Configuration
@Slf4j
public class WebMvcConfiguration extends WebMvcConfigurationSupport {

    @Autowired
    private JwtTokenAdminInterceptor jwtTokenAdminInterceptor;

    @Autowired
    private JwtTokenUserInterceptor jwtTokenUserInterceptor;

    /**
     * 注册自定义拦截器
     */
    protected void addInterceptors(InterceptorRegistry registry) {
        log.info("开始注册自定义拦截器...");
        registry.addInterceptor(jwtTokenAdminInterceptor)
                .addPathPatterns("/admin/**")
                .excludePathPatterns("/admin/employee/login");

        registry.addInterceptor(jwtTokenUserInterceptor)
                .addPathPatterns("/user/**")
                .excludePathPatterns("/user/user/login")
                .excludePathPatterns("/user/shop/status");
    }

    /**
     * 通过 knife4j 生成接口文档
     * 区分管理端（docket1）和用户端接口（docket2）
     */
    @Bean
    public Docket docket1() {
        log.info("准备生成接口文档...");
        ApiInfo apiInfo = new ApiInfoBuilder()
                .title("万酒屋 VinoHouse 项目接口文档")
                .version("2.0")
                .description("万酒屋 VinoHouse 项目接口文档")
                .build();

        Docket docket = new Docket(DocumentationType.SWAGGER_2)
                .groupName("管理端接口")
                .apiInfo(apiInfo)
                .select()
                // 指定生成接口需要扫描的包
                .apis(RequestHandlerSelectors.basePackage("com.VinoHouse.controller.admin"))
                .paths(PathSelectors.any())
                .build();

        return docket;
    }

    @Bean
    public Docket docket2() {
        log.info("准备生成接口文档...");
        ApiInfo apiInfo = new ApiInfoBuilder()
                .title("万酒屋 VinoHouse 项目接口文档")
                .version("2.0")
                .description("万酒屋 VinoHouse 项目接口文档")
                .build();

        Docket docket = new Docket(DocumentationType.SWAGGER_2)
                .groupName("用户端接口")
                .apiInfo(apiInfo)
                .select()
                // 指定生成接口需要扫描的包
                .apis(RequestHandlerSelectors.basePackage("com.VinoHouse.controller.user"))
                .paths(PathSelectors.any())
                .build();

        return docket;
    }

    /**
     * 设置静态资源映射，主要是访问接口文档（html、js、css）
     */
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        log.info("开始设置静态资源映射...");
        registry.addResourceHandler("/doc.html").addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
    }

    /**
     * 扩展 Spring MVC 框架的消息转化器，统一对日期类型进行格式处理
     */
    protected void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        log.info("扩展消息转换器...");
        // 创建一个消息转换器对象：系统自带 8 个消息转换器
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        // 需要为消息转换器设置一个对象转换器，对象转换器可以将 Java 对象序列化为 json 数据
        converter.setObjectMapper(new JacksonObjectMapper());
        // 将自己的消息转化器加入容器中，位置在第 1 位（索引：0）。如果不加索引默认在最后，一般用不到最后的消息转换器。
        converters.add(0, converter);
    }
}

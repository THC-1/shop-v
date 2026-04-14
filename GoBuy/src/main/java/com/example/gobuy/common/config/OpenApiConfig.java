package com.example.gobuy.common.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Value("${server.port:8080}")
    private String serverPort;

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("GoBuy 商城 API 文档")
                        .version("1.0.0")
                        .description("""
                                ## GoBuy 商城系统 RESTful API 接口文档
                                
                                ### 技术栈
                                - Spring Boot 3.5.13
                                - MyBatis-Plus
                                - MySQL + Redis
                                - JWT 认证
                                
                                ### 接口说明
                                - 所有需要认证的接口需要在 Header 中添加 `Authorization: Bearer {token}`
                                - 点击接口右上角的锁图标可以全局设置 Token
                                - 绿色接口：无需认证（如登录、注册、商品浏览）
                                - 红色接口：需要认证（如订单、购物车、地址管理）
                                """)
                        .contact(new Contact()
                                .name("GoBuy Team")
                                .email("support@gobuy.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:" + serverPort)
                                .description("本地开发环境"),
                        new Server()
                                .url("http://localhost:" + serverPort + "/api/v1")
                                .description("API v1 版本")))
                .addSecurityItem(new SecurityRequirement().addList("Bearer Authentication"))
                .components(new Components()
                        .addSecuritySchemes("Bearer Authentication", createSecurityScheme()));
    }

    private SecurityScheme createSecurityScheme() {
        return new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")
                .description("请输入 JWT Token，格式：`eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...`");
    }

    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("1. 公开接口")
                .pathsToMatch(
                        "/api/v1/users/login",
                        "/api/v1/users/register",
                        "/api/v1/products/**",
                        "/api/v1/scenarios/**"
                )
                .build();
    }

    @Bean
    public GroupedOpenApi userAuthApi() {
        return GroupedOpenApi.builder()
                .group("2. 用户认证接口")
                .pathsToMatch(
                        "/api/v1/users/me",
                        "/api/v1/users/logout"
                )
                .build();
    }

    @Bean
    public GroupedOpenApi authApi() {
        return GroupedOpenApi.builder()
                .group("3. 其他认证接口")
                .pathsToMatch(
                        "/api/v1/carts/**",
                        "/api/v1/orders/**",
                        "/api/v1/payments/**",
                        "/api/v1/addresses/**"
                )
                .build();
    }
}

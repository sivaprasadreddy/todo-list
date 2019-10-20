package com.sivalabs.todo.config;

import lombok.Data;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "application")
@Getter
public class ApplicationProperties {
    private static final Long DEFAULT_JWT_TOKEN_EXPIRES = 604800L;

    private JwtConfig jwt = new JwtConfig();

    @Data
    public static class JwtConfig {
        private String issuer = "todo";
        private String header = "Authorization";
        private Long expiresIn = DEFAULT_JWT_TOKEN_EXPIRES;
        private String secret = "";
    }
}

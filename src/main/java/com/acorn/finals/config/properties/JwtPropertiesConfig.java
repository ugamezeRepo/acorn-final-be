package com.acorn.finals.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;


@ConfigurationProperties(prefix = "jwt")
@Configuration
@Data
public class JwtPropertiesConfig {
    private String name;
    private long expiration;
}

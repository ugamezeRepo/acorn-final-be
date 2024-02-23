package com.acorn.finals.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;


@ConfigurationProperties(prefix = "cors")
@Configuration
@Data
public class CorsPropertiesConfig {
    private String[] allowedOrigins;
}

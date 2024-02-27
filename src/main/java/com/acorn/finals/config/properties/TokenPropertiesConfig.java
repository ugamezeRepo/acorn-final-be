package com.acorn.finals.config.properties;

import com.acorn.finals.model.property.TokenProperty;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties(prefix = "token")
@Configuration
@Data
public class TokenPropertiesConfig {
    private TokenProperty accessToken;
    private TokenProperty refreshToken;
}

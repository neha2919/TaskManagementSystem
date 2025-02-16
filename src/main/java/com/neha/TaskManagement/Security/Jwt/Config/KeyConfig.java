package com.neha.TaskManagement.Security.Jwt.Config;

import io.jsonwebtoken.security.Keys;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;

@Data
@Component
@ConfigurationProperties("app.security.key")
public class KeyConfig {
    private String signingSecret;
    private String type;

    public Key getKey(){
        byte [] keyBytes = signingSecret.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}

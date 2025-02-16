package com.neha.TaskManagement.Security.Jwt.Config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
@ConfigurationProperties("app.security.jwt")
public class JwtConfig {
    private String authType;
    private Integer atExpirationMin;
    private Integer rtExpirationMin;
    private String header;
    private String issuer;
}

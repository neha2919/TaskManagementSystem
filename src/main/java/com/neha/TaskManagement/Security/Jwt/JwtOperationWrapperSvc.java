package com.neha.TaskManagement.Security.Jwt;

import com.neha.TaskManagement.Dtos.UserDto;
import com.neha.TaskManagement.Security.Jwt.Config.JwtConfig;
import com.neha.TaskManagement.Security.Jwt.Config.KeyConfig;
import com.neha.TaskManagement.Security.Jwt.Model.JwtUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
@Slf4j
public class JwtOperationWrapperSvc {

    @Autowired
    private KeyConfig keyConfig;
    @Autowired
    private JwtConfig jwtConfig;
    public String generateToken(UserDto user){
        if (user==null){
            return Jwts.builder()
                    .claim("authorities",List.of("SERVER_TOKEN"))
                    .subject(user.getFullName())
                    .issuer(jwtConfig.getIssuer())
                    .id("SERVER")
                    .issuedAt(new Date())
                    .expiration(Date.from(LocalDateTime.now()
                            .plusMinutes(5)
                            .atZone(ZoneId.systemDefault()).toInstant()))
                    .signWith(keyConfig.getKey())
                    .compact();
        }
        return Jwts.builder()
                .issuer(jwtConfig.getIssuer())
                .id(user.getUsername())
                .subject(user.getFullName())
                .issuedAt(new Date())
                .claim("authorities",user.getRoles())
                .expiration(Date.from(LocalDateTime.now()
                        .plusMinutes(jwtConfig.getAtExpirationMin())
                        .atZone(ZoneId.systemDefault()).toInstant()))
                .signWith(keyConfig.getKey())
                .compact();
    }

    public String generateRefreshToken(UserDto user){
        return Jwts.builder()
                .id(user.getUsername())
                .subject(user.getFullName())
                .issuer(jwtConfig.getIssuer())
                .issuedAt(new Date())
                .expiration(Date.from(LocalDateTime.now()
                        .plusMinutes(jwtConfig.getRtExpirationMin())
                        .atZone(ZoneId.systemDefault())
                        .toInstant()))
                .signWith(keyConfig.getKey())
                .compact();
    }

    public JwtUser validateToken(String token){
        Jws<Claims> jws;
        try{
            jws = Jwts.parser().setSigningKey(keyConfig.getKey())
                    .requireIssuer(jwtConfig.getIssuer())
                    .build()
                    .parseClaimsJws(token);
            Claims claims = jws.getBody();

            JwtUser jwtUser = new JwtUser();
            jwtUser.setFullName(claims.getSubject());
            jwtUser.setPrincipal(claims.getId());
            List<String> authorities = (List<String>)(claims.get("authorities"));
            jwtUser.setAuthorities(authorities==null?new ArrayList<>():authorities);

            return jwtUser;
        }catch (JwtException jwtException){
            log.error("Jwt Token Exception: {}",jwtException.getMessage());
            return null;
        }
    }
}

package com.neha.TaskManagement.Configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Collections;
import java.util.List;

@Configuration
public class AppSecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .cors(cors -> cors.configurationSource(config ->{
                    CorsConfiguration configuration = new CorsConfiguration();
                    configuration.setExposedHeaders(List.of("Authorization"));
                    configuration.setAllowedOrigins(Collections.singletonList("*"));
                    configuration.setAllowedMethods(List.of("GET","POST","PUT","PATCH","DELETE"));
                    configuration.setExposedHeaders(Collections.singletonList("*"));

                    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
                    source.registerCorsConfiguration("/**",configuration);

                    return configuration;
                }))
                .authorizeHttpRequests(authorize -> authorize.anyRequest().permitAll())
                .httpBasic(Customizer.withDefaults());

        return httpSecurity.build();
    }
}

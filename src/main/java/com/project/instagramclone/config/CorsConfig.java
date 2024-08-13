package com.project.instagramclone.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Collection;
import java.util.Collections;

// https://medium.com/@byeongsoon94/spring-boot-cors-%EC%9D%B4%EC%8A%88%ED%95%B4%EA%B2%B0%ED%95%98%EA%B8%B0-webmvcconfigurer%EB%A5%BC-%ED%86%B5%ED%95%9C-%EC%84%A4%EC%A0%95-635933248b91
// CORS를 enable 해줘야 H2 콘솔이나 Postman에서 결과 확인 가능

@Configuration
public class CorsConfig {
    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(Collections.singletonList("http://localhost:3000"));
        config.setAllowedMethods(Collections.singletonList("*"));
        config.setAllowCredentials(true);
        config.setAllowedHeaders(Collections.singletonList("*"));
        config.setMaxAge(3600L);

        config.setExposedHeaders(Collections.singletonList("Set-Cookie"));
        config.setExposedHeaders(Collections.singletonList("Authorization"));

        config.addAllowedOriginPattern("*");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");

        source.registerCorsConfiguration("/api/**", config);
        return new CorsFilter(source);
    }
}
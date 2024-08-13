package com.project.instagramclone.config;

import com.project.instagramclone.jwt.JWTFilter;
import com.project.instagramclone.jwt.JWTUtil;
import com.project.instagramclone.oauth2.CustomOAuth2UserService;
import com.project.instagramclone.oauth2.CustomSuccessHandler;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Collections;

@EnableWebSecurity
@EnableMethodSecurity // @PreAuthorize 어노테이션을 메서드단위로 추가하기 위함
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomOAuth2UserService auth2UserService;
    private final CustomSuccessHandler customSuccessHandler;
    private final JWTUtil jwtUtil;
    // private final CorsFilter corsFilter;

    /*
    @RequiredArgsConstructor 어노테이션 사용으로 주석처리 함
    public SecurityConfig( // 생성한 클래스들을 주입받음
        CustomOAuth2UserService auth2UserService,
        CustomSuccessHandler customSuccessHandler,
        JwtUtil jwtUtil,
        CorsFilter corsFilter
    ) {
        this.auh2UserService = auth2UserService;
        this.customSuccessHandler = customSuccessHandler;
        this.jwtUtil = jwtUtil;
        this.corsFilter = corsFilter;
    }
    */

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, CustomOAuth2UserService customOAuth2UserService) throws Exception {
        http
                // token을 사용하는 방식이기 때문에 csrf를 disable
                // JWT는 세션을 stateless 하게 관리함
                // 세션 방식의 경우 세션이 고정되어야 하기에 csrf enable
                .csrf(AbstractHttpConfigurer::disable)

                // JWT를 이용해 로그인을 하기에 formLogin과 basic 인증 방식 비활성화
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)

                // CORS Filter 등록
                // .addFilterBefore(corsFilter, UsernamePasswordAuthenticationFilter.class)
                .cors(corsCustomizer -> corsCustomizer.configurationSource(new CorsConfigurationSource() {
                    @Override
                    public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {

                        CorsConfiguration configuration = new CorsConfiguration();

                        configuration.setAllowedOrigins(Collections.singletonList("http://localhost:3000/"));
                        configuration.setAllowedMethods(Collections.singletonList("*"));
                        configuration.setAllowCredentials(true);
                        configuration.setAllowedHeaders(Collections.singletonList("*"));
                        configuration.setMaxAge(3600L);

                        configuration.setExposedHeaders(Collections.singletonList("Set-Cookie"));
                        // configuration.setExposedHeaders(Collections.singletonList("access"));
                        configuration.setExposedHeaders(Collections.singletonList("Authorization"));

                        return configuration;
                    }
                }))

                // JwtFilter 등록
                // .addFilterBefore(new JwtFilter(jwtUtil), LoginFilter.class)
                .addFilterBefore(new JWTFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class)
                // 커스텀한 로그인 필터를 UsernamePasswordAuthenticationFilter 자리에 등록
                // .addFilterAt(new LoginFilter(authenticationManager(authenticationConfiguration), jwtUtil), UsernamePasswordAuthenticationFilter.class)

                // HttpServletRequest를 사용하는 요청에 대한 접근제한 설정
                .authorizeHttpRequests(authorizeHttpRequests -> authorizeHttpRequests
                        .requestMatchers("/api/signup", "/api/login").permitAll() // 로그인과 토큰 요청 시 토큰이 없기에 허용
                        .requestMatchers(PathRequest.toH2Console()).permitAll() // H2콘솔 허용
                        .requestMatchers("/api/oauth/google/login").permitAll() // 구글 로그인
                        // .anyRequest().permitAll()
                        .anyRequest().authenticated() // 나머지 요청에 대해서 인증 필요
                )

                //oauth2
                .oauth2Login((oauth2) -> oauth2
                        .userInfoEndpoint((userInfoEndpointConfig) -> userInfoEndpointConfig
                                .userService(customOAuth2UserService))
                        .defaultSuccessUrl("http://localhost:3000") // 로그인 성공 시 리다이렉트 URI
                )

                // 세션을 사용하지 않기 때문에 STATELESS로 설정
                .sessionManagement(sessionManagement ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                // enable h2-console
                .headers(headers ->
                        headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin)
                );

        return http.build();
    }
}
package com.project.instagramclone.config;

import com.project.instagramclone.handler.CustomFormSuccessHandler;
import com.project.instagramclone.handler.CustomLogoutFilter;
import com.project.instagramclone.handler.CustomOAuth2SuccessHandler;
import com.project.instagramclone.jwt.JWTFilter;
import com.project.instagramclone.jwt.JWTUtil;
import com.project.instagramclone.repository.token.RefreshRepository;
import com.project.instagramclone.service.form.CustomUserDetailsService;
import com.project.instagramclone.service.oauth2.CustomOAuth2UserService;
import com.project.instagramclone.service.oauth2.OAuth2UserService;
import com.project.instagramclone.service.token.RefreshTokenService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;

@EnableWebSecurity
@EnableMethodSecurity // @PreAuthorize 어노테이션을 메서드단위로 추가하기 위함
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JWTUtil jwtUtil;
    private final CustomUserDetailsService customUserDetailsService;
    private final CustomOAuth2UserService customOAuth2UserService;
    private final OAuth2UserService oAuth2UserService;
    private final RefreshTokenService refreshTokenService;
    private final RefreshRepository refreshRepository;
    public static final String[] swaggerArray = {
            "/api-docs",
            "/swagger-ui-custom.html",
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/api-docs/**",
            "/swagger-ui.html",
            "/swagger-custom-ui.html",
            "/swagger-ui/**"
    };

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationFailureHandler authenticationFailureHandler(){
        return new AuthenticationFailureHandler() {
            @Override
            public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
                System.out.println("exception = " + exception);
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
            }
        };
    }

    @Bean
    public SecurityFilterChain securityfilterChain(HttpSecurity http, CustomUserDetailsService customUserDetailsService) throws Exception {
        http
                // token을 사용하는 방식이기 때문에 csrf를 disable
                // JWT는 세션을 stateless 하게 관리함
                // 세션 방식의 경우 세션이 고정되어야 하기에 csrf enable
                .csrf(AbstractHttpConfigurer::disable)

                // JWT를 이용해 로그인을 하기에 basic 인증 방식 비활성화
                .httpBasic(AbstractHttpConfigurer::disable)

                // formLogin 커스터마이즈
                .formLogin((form) -> form.loginPage("/api/v1/login")
                        .loginProcessingUrl("/api/v1/login")
                        .successHandler(new CustomFormSuccessHandler(jwtUtil, refreshTokenService))
                        .failureHandler(authenticationFailureHandler())
                        .permitAll())

                // OAuth2
                .oauth2Login((oauth2) -> oauth2
                        .loginPage("/api/v1/login")
                        .userInfoEndpoint((userInfoEndpointConfig) -> userInfoEndpointConfig
                                .userService(customOAuth2UserService))
                        .successHandler(new CustomOAuth2SuccessHandler(jwtUtil, refreshTokenService))
                        .failureHandler(authenticationFailureHandler())
                        .permitAll())
                        // .defaultSuccessUrl("http://localhost:3000") // 로그인 성공 시 리다이렉트 URI


                // logout
                .logout((auth) -> auth
                        .logoutSuccessUrl("/")
                        .permitAll())

                // CORS Filter 등록
                // .addFilterBefore(corsFilter, UsernamePasswordAuthenticationFilter.class)
                .cors((cors) -> cors.configurationSource(new CorsConfigurationSource() {
                    @Override
                    public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
                        CorsConfiguration configuration = new CorsConfiguration();
                        configuration.setAllowedOrigins(Collections.singletonList("http://localhost:3000/"));
                        configuration.setAllowedMethods(Collections.singletonList("*"));
                        configuration.setAllowCredentials(true);
                        configuration.setAllowedHeaders(Collections.singletonList("*"));
                        configuration.setMaxAge(3600L);

                        configuration.setExposedHeaders(Collections.singletonList("access"));

                        return configuration;
                    }
                }))

                // HttpServletRequest를 사용하는 요청에 대한 접근제한 설정
                .authorizeHttpRequests(authorizeHttpRequests -> authorizeHttpRequests
                        .requestMatchers(
                                // 회원가입 및 로그인/로그아웃 관련 로직
                                "/",
                                "/api/v1/login",
                                "/api/v1/join",
                                "/logout",
                                "/api/v1/oauth2-jwt-header",

                                "/set-nickname",
                                "/api/v1/user/update",
                                "/api/v1/user/change-password",

                                "/error"
                        ).permitAll() // 허용
                        .requestMatchers(swaggerArray).permitAll() // swagger 페이지 접근 허용
                        .requestMatchers(PathRequest.toH2Console()).permitAll() // H2콘솔 허용
                        .requestMatchers("/api/v1/admin").hasRole("ADMIN")
                        // .requestMatchers("/api/oauth/google/login").permitAll() // 구글 로그인
                        .anyRequest().authenticated() // 나머지 요청에 대해서 인증 필요
                )

                // 인가되지 않은 사용자에 대한 exception -> 프론트엔드로 코드 응답
                .exceptionHandling((exception) -> exception
                        .authenticationEntryPoint((request, response, authException) -> {
                            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                        })
                )

                // JwtFilter 등록
                .addFilterBefore(new JWTFilter(jwtUtil, customUserDetailsService, oAuth2UserService), UsernamePasswordAuthenticationFilter.class)

                // custom logout filter 등록
                .addFilterBefore(new CustomLogoutFilter(jwtUtil, refreshRepository), LogoutFilter.class)

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
package com.project.instagramclone.oauth2;

import com.project.instagramclone.entity.User;
import jakarta.security.auth.message.AuthException;
import lombok.Builder;

import java.util.Map;

public class OAuth2UserInfo {
    @Builder
    public record OAuth2UserInfo(
            String name,
            String email,
            String profile
    ) {

        public static OAuth2UserInfo of(String registrationId, Map<String, Object> attributes) {
            return switch (registrationId) { // registration id별로 userInfo 생성
                case "google" -> ofGoogle(attributes);
                default -> throw new AuthException(ILLEGAL_REGISTRATION_ID);
            };
        }

        private static OAuth2UserInfo ofGoogle(Map<String, Object> attributes) {
            return OAuth2UserInfo.builder()
                    .name((String) attributes.get("name"))
                    .email((String) attributes.get("email"))
                    .profile((String) attributes.get("picture"))
                    .build();
        }

        public User toEntity() {
            return User.builder()
                    .uid(name)
                    .email(email)
                    .profile(profile)
                    .memberKey(KeyGenerator.generateKey())
                    .build();
        }
    }
}

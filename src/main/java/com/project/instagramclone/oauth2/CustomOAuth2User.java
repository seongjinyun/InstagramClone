package com.project.instagramclone.oauth2;

import com.project.instagramclone.dto.SignUpDto;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

public class CustomOAuth2User implements OAuth2User {

    private final SignUpDto signUpDTO;

    public CustomOAuth2User(SignUpDto signUpDTO) {
        this.signUpDTO = signUpDTO;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return null;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // 빈 컬렉션을 반환하여 role 값을 가져오지 않도록 설정
        return Collections.emptyList();
    }

    @Override
    public String getName() {
        return signUpDTO.getNickname();
    }

    public String getUsername() {
        return signUpDTO.getEmail();
    }
}
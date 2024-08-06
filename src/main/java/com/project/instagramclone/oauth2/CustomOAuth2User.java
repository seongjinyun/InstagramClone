package com.project.instagramclone.oauth2;

import com.project.instagramclone.dto.UserDto;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

public class CustomOAuth2User implements OAuth2User {

    private final UserDto userDTO;

    public CustomOAuth2User(UserDto userDTO) {

        this.userDTO = userDTO;
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

        return userDTO.getNickname();
    }

    public String getUsername() {

        return userDTO.getEmail();
    }
}
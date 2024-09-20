package com.project.instagramclone.dto.oauth2;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OAuth2UserDto {
    private String username;
    private String nickname;
    private String name;
    private String email;
    private String role;

    @Builder
    public OAuth2UserDto(String username, String nickname, String email, String role) {
        this.username = username;
        this.nickname = nickname;
        this.email = email;
        this.role = role;
    }
}

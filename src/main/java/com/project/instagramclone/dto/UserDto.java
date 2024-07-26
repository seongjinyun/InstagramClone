package com.project.instagramclone.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import com.project.instagramclone.entity.User;

import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    // 회원가입 시 사용

    @NotNull
    @Size(min = 3, max = 50)
    private String uid;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotNull
    @Size(min = 3, max = 100)
    private String password;

    @NotNull
    @Size(min = 3, max = 50)
    private String nickname;

    @NotNull
    @Size(min = 3, max = 50)
    private String email;

    public static UserDto from(User user) {
        if(user == null) return null;

        return UserDto.builder()
                .uid(user.getUid())
                .nickname(user.getNickname())
                .build();
    }
    public static User toEntity(UserDto userDto) {
        return User.builder()
                .uid(userDto.getUid())
                .nickname(userDto.getNickname())
                .email(userDto.getEmail())
                .activated(true) // 회원가입 시 기본으로 활성화 설정
                .build();
    }
}
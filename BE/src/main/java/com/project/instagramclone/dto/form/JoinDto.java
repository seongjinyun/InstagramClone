package com.project.instagramclone.dto.form;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.instagramclone.entity.form.FormUserEntity;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JoinDto {

    // form 방식의 회원가입 시 사용

    @NotNull
    @Size(min = 3, max = 50)
    private String username;

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

    public static JoinDto from(FormUserEntity formUserEntity) {

        if(formUserEntity == null) return null;

        return JoinDto.builder()
                .username(formUserEntity.getUsername())
                .password(formUserEntity.getPassword())
                .nickname(formUserEntity.getNickname())
                .email(formUserEntity.getEmail())
                // 회원가입 시 프로필 이미지 추가하지 않음
                // 회원가입 시 회원 권한(role)으로 설정
                .build();
    }

}

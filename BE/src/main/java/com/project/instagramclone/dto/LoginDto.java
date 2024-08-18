package com.project.instagramclone.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginDto {

    // 로그인 시 사용

    @NotNull
    @Size(min = 3, max = 50)
    private String username;

    @NotNull
    @Size(min = 3, max = 100)
    private String password;

}
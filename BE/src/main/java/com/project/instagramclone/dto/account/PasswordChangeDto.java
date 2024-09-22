package com.project.instagramclone.dto.account;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PasswordChangeDto {

    private String currentPassword;
    private String newPassword;

}

package com.project.instagramclone.dto.account;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccountUpdateDto {

    private String nickname;
    private String profilePic;
    private String bio;

}

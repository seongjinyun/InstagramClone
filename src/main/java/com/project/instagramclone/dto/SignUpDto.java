package com.project.instagramclone.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.instagramclone.entity.Member;
import com.project.instagramclone.entity.MemberDetail;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SignUpDto {

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

    @NotNull
    private int sns_id;

    public static SignUpDto from(Member member, MemberDetail memberDetail) {
        if(member == null) return null;

        return SignUpDto.builder()
                .uid(member.getUid())
                .password(memberDetail.getPassword())
                .nickname(member.getNickname())
                .email(member.getEmail())
                .sns_id(member.getSns().getSnsId())
                // 회원 가입 시 프로필 이미지는 추가하지 않음. 나중에 회원정보 수정 시 처리
                .build();
    }
}
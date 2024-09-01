package com.project.instagramclone.service.form;

import com.project.instagramclone.dto.form.JoinDto;
import com.project.instagramclone.entity.form.FormUserEntity;
import com.project.instagramclone.entity.member.MemberEntity;
import com.project.instagramclone.repository.form.FormUserRepository;
import com.project.instagramclone.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class JoinService {

    private final FormUserRepository formUserRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final MemberRepository memberRepository;

    public void join(JoinDto joinDto) {

        Boolean isExist = formUserRepository.existsByUsername(joinDto.getUsername());

        if (isExist) {
            System.out.println("이미 존재하는 회원입니다.");
            return;
        }

        // 1. 새로운 MemberEntity 생성 및 저장
        MemberEntity memberEntity = new MemberEntity();
        memberEntity = memberRepository.save(memberEntity);

        FormUserEntity formUserEntity = FormUserEntity
                .builder()
                .username(joinDto.getUsername())
                .password(bCryptPasswordEncoder.encode(joinDto.getPassword()))
//                .nickname(joinDto.getNickname())
//                .email(joinDto.getEmail())
                .activated(true)
                .role("ROLE_USER")
                .memberEntity(memberEntity) // MemberEntity와 연결
                .build();

        formUserRepository.save(formUserEntity);
    }
}
package com.project.instagramclone.service.form;

import com.project.instagramclone.dto.form.JoinDto;
import com.project.instagramclone.entity.form.FormUserEntity;
import com.project.instagramclone.repository.form.FormUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class JoinService {

    private final FormUserRepository formUserRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public void join(JoinDto joinDto) {

        Boolean isExist = formUserRepository.existsByUsername(joinDto.getUsername());

        if (isExist) {
            System.out.println("이미 존재하는 회원입니다.");
            return;
        }

        FormUserEntity formUserEntity = FormUserEntity
                .builder()
                .username(joinDto.getUsername())
                .password(bCryptPasswordEncoder.encode(joinDto.getPassword()))
                .role("ROLE_USER")
                .build();

        formUserRepository.save(formUserEntity);
    }
}
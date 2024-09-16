package com.project.instagramclone.service.form;

import com.project.instagramclone.dto.form.CustomUserDetails;
import com.project.instagramclone.entity.form.FormUserEntity;
import com.project.instagramclone.repository.form.FormUserRepository;
import com.project.instagramclone.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class FormUserService {

    private final FormUserRepository formUserRepository;

    // 닉네임으로 username을 조회하는 메서드 추가
    public String getUsernameByNickname(String nickname) {
        Optional<FormUserEntity> userEntity = formUserRepository.findByNickname(nickname);
        if (userEntity.isPresent()) {
            return userEntity.get().getUsername();
        } else {
            throw new IllegalArgumentException("해당 닉네임의 사용자가 존재하지 않습니다.");
        }
    }

}

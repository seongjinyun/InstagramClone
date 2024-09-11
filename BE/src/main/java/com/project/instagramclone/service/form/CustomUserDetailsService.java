package com.project.instagramclone.service.form;

import com.project.instagramclone.dto.form.CustomUserDetails;
import com.project.instagramclone.entity.form.FormUserEntity;
import com.project.instagramclone.repository.form.FormUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final FormUserRepository formUserRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<FormUserEntity> formUser = formUserRepository.findByUsername(username);
        if (formUser.isPresent()) {
            return new CustomUserDetails(formUser.get());
        }
        throw new UsernameNotFoundException("회원 정보가 존재하지 않습니다.");
    }

    public Long getMemberIdByUsername(String username) {
        // username으로 FormUserEntity 조회
        FormUserEntity formUser = formUserRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));
        // FormUserEntity에서 memberEntity의 memberId 반환
        return formUser.getMemberEntity().getMemberId();
    }
}

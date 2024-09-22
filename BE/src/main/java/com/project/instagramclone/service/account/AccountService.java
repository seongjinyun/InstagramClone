package com.project.instagramclone.service.account;

import com.project.instagramclone.dto.account.AccountUpdateDto;
import com.project.instagramclone.dto.account.PasswordChangeDto;
import com.project.instagramclone.entity.form.FormUserEntity;
import com.project.instagramclone.entity.oauth2.OAuth2UserEntity;
import com.project.instagramclone.repository.form.FormUserRepository;
import com.project.instagramclone.repository.oauth2.OAuth2UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class AccountService {

    private final FormUserRepository formUserRepository;
    private final OAuth2UserRepository oAuth2UserRepository;
    private final PasswordEncoder passwordEncoder;

    // 사용자 정보 수정 로직
    public void updateAccount(Long memberId, AccountUpdateDto accountUpdateDto) {
        Optional<FormUserEntity> formUser = formUserRepository.findByMemberEntity_MemberId(memberId);
        Optional<OAuth2UserEntity> oauthUser = oAuth2UserRepository.findByMemberEntity_MemberId(memberId);

        if (formUser.isPresent()) {
            FormUserEntity user = formUser.get();
            user.setNickname(accountUpdateDto.getNickname());
//            user.setProfilePic(accountUpdateDto.getProfilePic());
//            user.setBio(accountUpdateDto.getBio());
            formUserRepository.save(user);
        } else if (oauthUser.isPresent()) {
            OAuth2UserEntity user = oauthUser.get();
            user.setNickname(accountUpdateDto.getNickname());
//            user.setProfilePic(accountUpdateDto.getProfilePic());
//            user.setBio(accountUpdateDto.getBio());
            oAuth2UserRepository.save(user);
        }
    }

    // 비밀번호 변경 로직
    public boolean changePassword(Long memberId, PasswordChangeDto passwordChangeDto) {
        Optional<FormUserEntity> formUser = formUserRepository.findByMemberEntity_MemberId(memberId);

        if (formUser.isPresent()) {
            FormUserEntity user = formUser.get();
            if (passwordEncoder.matches(passwordChangeDto.getCurrentPassword(), user.getPassword())) {
                user.setPassword(passwordEncoder.encode(passwordChangeDto.getNewPassword()));
                formUserRepository.save(user);
                return true;
            }
        }
        return false;
    }
}

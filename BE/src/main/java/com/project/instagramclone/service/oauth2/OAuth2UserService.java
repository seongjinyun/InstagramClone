package com.project.instagramclone.service.oauth2;

import com.project.instagramclone.dto.oauth2.CustomOAuth2User;
import com.project.instagramclone.dto.oauth2.OAuth2UserDto;
import com.project.instagramclone.entity.oauth2.OAuth2UserEntity;
import com.project.instagramclone.repository.oauth2.OAuth2UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class OAuth2UserService {

    private final OAuth2UserRepository oAuth2UserRepository;

    // 닉네임으로 username을 조회하는 메서드 추가
    public String getUsernameByNickname(String nickname) {
        Optional<OAuth2UserEntity> userEntity = oAuth2UserRepository.findByNickname(nickname);
        if (userEntity.isPresent()) {
            return userEntity.get().getUsername();
        } else {
            throw new IllegalArgumentException("해당 닉네임의 사용자가 존재하지 않습니다.");
        }
    }

//    public Optional<OAuth2UserEntity> findByUsername(String username) {
//        return oAuth2UserRepository.findByUsername(username);
//    }

    public CustomOAuth2User findByUsername(String username) {
        OAuth2UserEntity entity = oAuth2UserRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("OAuth2 사용자 정보를 찾을 수 없습니다."));
        OAuth2UserDto userDto = OAuth2UserDto.builder()
                .username(entity.getUsername())
                .nickname(entity.getNickname())
                .email(entity.getEmail())
                .role(entity.getRole())
                .build();
        return new CustomOAuth2User(userDto);
    }

    public int updateNickname(String username, String nickname) {
        return oAuth2UserRepository.updateNickname(username, nickname);  // 명시적으로 update 쿼리 실행
    }


}

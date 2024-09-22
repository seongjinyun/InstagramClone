package com.project.instagramclone.service.oauth2;

import com.project.instagramclone.dto.form.CustomUserDetails;
import com.project.instagramclone.dto.oauth2.CustomOAuth2User;
import com.project.instagramclone.dto.oauth2.GoogleResponse;
import com.project.instagramclone.dto.oauth2.OAuth2Response;
import com.project.instagramclone.dto.oauth2.OAuth2UserDto;
import com.project.instagramclone.entity.form.FormUserEntity;
import com.project.instagramclone.entity.member.MemberEntity;
import com.project.instagramclone.entity.oauth2.OAuth2UserEntity;
import com.project.instagramclone.repository.member.MemberRepository;
import com.project.instagramclone.repository.oauth2.OAuth2UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;

import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    private final OAuth2UserRepository oAuth2UserRepository;
    private final MemberRepository memberRepository;

    @Transactional
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        // userRequest -> registration 정보
        OAuth2User oAuth2User = super.loadUser(userRequest);
        String clientName = userRequest.getClientRegistration().getClientName();

        OAuth2Response response = null;
        Map<String, Object> attributes = oAuth2User.getAttributes();

        // 존재하는 provider 인지 확인
        if (clientName.equals("google")) {
            response = (OAuth2Response) new GoogleResponse(attributes);
        } else {
            return null;
        }

        // provider name + provider Id 로 username(식별자) 생성
        String username = response.getProvider() + " " + response.getProviderId();
        CustomOAuth2User customOAuth2User = null;
        String role = "ROLE_USER";

        // DB save
        OAuth2UserEntity oAuth2UserEntity = saveUser(response, username, role);
        Long memberId = oAuth2UserEntity.getMemberEntity().getMemberId();

        // Entity 목적 순수하게 유지하기 위해서 dto 로 전달..
        OAuth2UserDto oAuth2UserDto = OAuth2UserDto.builder()
                .username(username)
                .nickname(response.getName())
                .email(response.getEmail())
                .role(role)
                .build();

        //  customOAuth2User = new CustomOAuth2User(oAuth2UserDto);
        // CustomOAuth2User에 memberId 설정
        customOAuth2User = new CustomOAuth2User(oAuth2UserDto, memberId);

        // 서버 내부에서 사용하기 위한 인증 정보
        return (OAuth2User) customOAuth2User;
    }

    /**
     * 이미 존재하는 경우 update
     * 존재하지 않는 경우 save
     */
    private OAuth2UserEntity saveUser(OAuth2Response response, String username, String role) {
        // DB 조회
        Optional<OAuth2UserEntity> isExist = oAuth2UserRepository.findByUsername(username);

        if (isExist.isPresent()) {
            // 사용자 업데이트 로직
            // isExist.stream();

            OAuth2UserEntity user = isExist.get();
            user.setNickname(response.getName());
            user.setEmail(response.getEmail());
            return oAuth2UserRepository.save(user);
        } else {
            MemberEntity memberEntity = new MemberEntity();
            memberEntity = memberRepository.save(memberEntity);

            OAuth2UserEntity oAuth2UserEntity = OAuth2UserEntity.builder()
                    .username(username)
                    .nickname(response.getName())
                    .email(response.getEmail())
                    .role(role)
                    .memberEntity(memberEntity)
                    .build();
            return oAuth2UserRepository.save(oAuth2UserEntity);
        }
    }
}

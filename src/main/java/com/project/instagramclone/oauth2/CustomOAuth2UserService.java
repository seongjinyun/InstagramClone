package com.project.instagramclone.oauth2;

import com.project.instagramclone.dto.UserDto;
import com.project.instagramclone.entity.Member;
import com.project.instagramclone.entity.Sns;
import com.project.instagramclone.repository.SnsRepository;
import com.project.instagramclone.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;
    private final SnsRepository snsRepository;

    //http://localhost:8080/oauth2/authorization/google 이 주소로 접속해야 로그인 했을 때 DB 저장되는 것 같음
    
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        System.out.println("loadUser 실행됨");

        OAuth2User oAuth2User = super.loadUser(userRequest);

        System.out.println("OAuth2User: " + oAuth2User);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        OAuth2Response oAuth2Response = null;
        if (registrationId.equals("google")) {
            oAuth2Response = new GoogleResponse(oAuth2User.getAttributes());
        }
        else {
            return null;
        }

        //리소스 서버에서 발급 받은 정보로 사용자를 특정할 아이디값을 만듬
        String nickname = oAuth2Response.getProvider()+" "+oAuth2Response.getProviderId();
        Member existData = memberRepository.findByNickname(nickname).orElse(null);

        if (existData == null) {
            Sns sns = snsRepository.findById(2).orElseThrow(() -> new IllegalArgumentException("SNS 정보가 없습니다."));
            Member member = new Member();
            member.setUid(oAuth2Response.getProviderId());
            member.setNickname(nickname);
            member.setEmail(oAuth2Response.getEmail());
            member.setSns(sns);
            memberRepository.save(member);
            System.out.println("로그인 성공, DB 저장 완료");

            UserDto userDTO = UserDto.builder()
                    .uid(oAuth2Response.getProviderId())
                    .nickname(nickname)
                    .email(oAuth2Response.getEmail())
                    .sns_id(sns.getSnsId())
                    .build();
            return new CustomOAuth2User(userDTO);
        } else {
            existData.setEmail(oAuth2Response.getEmail());
            memberRepository.save(existData);
            System.out.println("로그인 성공, DB 갱신 완료");

            UserDto userDTO = UserDto.builder()
                    .uid(existData.getUid())
                    .nickname(existData.getNickname())
                    .email(oAuth2Response.getEmail())
                    .sns_id(existData.getSns().getSnsId())
                    .build();
            return new CustomOAuth2User(userDTO);
        }
    }
}

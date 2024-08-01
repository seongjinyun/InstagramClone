package com.project.instagramclone.oauth2;

import com.project.instagramclone.dto.UserDto;
import com.project.instagramclone.entity.User;
import com.project.instagramclone.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(userRequest);

        System.out.println(oAuth2User);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        OAuth2Response oAuth2Response = null;
        if (registrationId.equals("google")) {

            oAuth2Response = new GoogleResponse(oAuth2User.getAttributes());
        }
        else {

            return null;
        }

        // 오류 = user entity에 username이 없음
        // username을 nickname으로 바꾸거나, username을 추가

        //리소스 서버에서 발급 받은 정보로 사용자를 특정할 아이디값을 만듬
        String username = oAuth2Response.getProvider()+" "+oAuth2Response.getProviderId();
        User existData = userRepository.findByNickname(username);

        if(existData == null){
            User user = new User();
            user.setNickname(username);
            user.setEmail(oAuth2Response.getEmail());

            userRepository.save(user);

            UserDto userDTO = new UserDto();
            userDTO.setNickname(username);
            userDTO.setEmail(oAuth2Response.getEmail());

            return new CustomOAuth2User(userDTO);
        }
        else {
            existData.setEmail(oAuth2Response.getEmail());
            existData.setNickname(oAuth2Response.getName());

            userRepository.save(existData);

            UserDto userDTO = new UserDto();
            userDTO.setNickname(existData.getNickname());
            userDTO.setEmail(oAuth2Response.getEmail());

            return new CustomOAuth2User(userDTO);
        }

    }
}

package com.project.instagramclone.service;

import com.project.instagramclone.dto.UserDto;
import com.project.instagramclone.entity.Sns;
import com.project.instagramclone.entity.User;
import com.project.instagramclone.entity.UserDetail;
import com.project.instagramclone.exception.DuplicateMemberException;
import com.project.instagramclone.repository.SnsRepository;
import com.project.instagramclone.repository.UserDetailRepository;
import com.project.instagramclone.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserDetailRepository userDetailRepository;
    private final SnsRepository snsRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, UserDetailRepository userDetailRepository, SnsRepository snsRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userDetailRepository = userDetailRepository;
        this.snsRepository = snsRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public UserDto signup(UserDto userDto) {

        if (userRepository.findByUid(userDto.getUid()).orElse(null) != null) {
            throw new DuplicateMemberException("이미 가입되어 있는 유저입니다.");
        }

        // sns_id를 이용해 Sns 엔티티를 조회
        Sns sns = snsRepository.findById(userDto.getSns_id())
                .orElseThrow(() -> new IllegalArgumentException("Invalid sns_id: " + userDto.getSns_id()));

        User user = User.builder()
                .uid(userDto.getUid())
                .nickname(userDto.getNickname())
                .email(userDto.getEmail())
                .sns(sns)
                .activated(true)
                .build();

        userRepository.save(user);

        UserDetail userDetail = UserDetail.builder()
                .user(user)
                .password(passwordEncoder.encode(userDto.getPassword()))
                .build();

        userDetailRepository.save(userDetail);

        return UserDto.from(user, userDetail);
    }
}
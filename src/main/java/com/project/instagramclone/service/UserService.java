package com.project.instagramclone.service;

import com.project.instagramclone.dto.UserDto;
import com.project.instagramclone.entity.User;
import com.project.instagramclone.entity.UserDetail;
import com.project.instagramclone.exception.DuplicateMemberException;
import com.project.instagramclone.exception.NotFoundMemberException;
import com.project.instagramclone.repository.UserDetailRepository;
import com.project.instagramclone.repository.UserRepository;
import com.project.instagramclone.util.SecurityUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserDetailRepository userDetailRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, UserDetailRepository userDetailRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userDetailRepository = userDetailRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public UserDto signup(UserDto userDto) {

//        if (userRepository.findByUid(userDto.getUid()).orElse(null) != null) {
//            throw new DuplicateMemberException("이미 가입되어 있는 유저입니다.");
//        }

        User user = User.builder()
                .uid(userDto.getUid())
                .nickname(userDto.getNickname())
                .email(userDto.getEmail())
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
package com.project.instagramclone.service;

import com.project.instagramclone.Repository.SnsRoleRepository;
import com.project.instagramclone.Repository.UserRepository;
import com.project.instagramclone.dto.UserDto;
import com.project.instagramclone.entity.SnsRole;
import com.project.instagramclone.entity.User;
import com.project.instagramclone.entity.UserDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Collections;

// User 엔티티의 서비스 클래스
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SnsRoleRepository snsRoleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    public User save(UserDto userDto) {
        User user = UserDto.toEntity(userDto);
        String encodedPassword = passwordEncoder.encode(userDto.getPassword());

        UserDetail userDetail = UserDetail.builder()
                .user(user)
                .password(encodedPassword)
                .build();
        user.setUserDetail(userDetail);

        SnsRole homepageRole = snsRoleRepository.findBySnsName("Homepage");

        user.setSnsRoles(Arrays.asList(homepageRole));

        return userRepository.save(user);
    }

    public User findByNickname(String nickname) {
        return userRepository.findByNickname(nickname).orElse(null);
    }
}
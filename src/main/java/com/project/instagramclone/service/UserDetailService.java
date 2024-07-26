package com.project.instagramclone.service;

import com.project.instagramclone.Repository.UserRepository;
import com.project.instagramclone.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 사용자 정보를 UserDetails로 반환
        User user = userRepository.findByNickname(username).orElseThrow(() ->
                new UsernameNotFoundException("User not found with username: " + username));

        return org.springframework.security.core.userdetails.User
                .withUsername(user.getNickname())
                .password(user.getUid()) // 비밀번호
                .authorities("USER") // 사용자 역할 설정
                .build();
    }
}
package com.project.instagramclone.service.test.form;

import com.project.instagramclone.dto.form.JoinDto;
import com.project.instagramclone.entity.test.UserEntity;
import com.project.instagramclone.repository.test.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class JoinService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public void join(JoinDto joinDto) {
        // join logic...
        Boolean isExist = userRepository.existsByUsername(joinDto.getUsername());

        if (isExist) {
            System.out.println("already exist user");
            return;
        }

        UserEntity userEntity = UserEntity
                .builder()
                .username(joinDto.getUsername())
                .password(bCryptPasswordEncoder.encode(joinDto.getPassword()))
                .role("ROLE_ADMIN")
                .build();

        userRepository.save(userEntity);
    }
}
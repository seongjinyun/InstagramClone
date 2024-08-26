package com.project.instagramclone.service.test.form;


import com.project.instagramclone.dto.form.CustomUserDetails;
import com.project.instagramclone.entity.test.UserEntity;
import com.project.instagramclone.repository.test.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByUsername(username);
        if (userEntity != null) {
            return new CustomUserDetails(userEntity);
        }
        throw new UsernameNotFoundException("User not found");
    }
}
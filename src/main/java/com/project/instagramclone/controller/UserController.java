package com.project.instagramclone.controller;

import com.project.instagramclone.dto.UserDto;
import com.project.instagramclone.entity.User;
import com.project.instagramclone.jwt.JwtUtil;
import com.project.instagramclone.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserDetailsService userDetailsService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserDto userDto) {
        System.out.println("Received registration data: " + userDto);
        User savedUser = userService.save(userDto);
        return ResponseEntity.ok(savedUser);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserDto userDto) {
        System.out.println("Received login data: " + userDto);
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userDto.getNickname(), userDto.getPassword())
        );

        UserDetails userDetails = userDetailsService.loadUserByUsername(userDto.getNickname());
        String jwt = jwtUtil.generateToken(userDetails.getUsername());

        System.out.println("Authentication successful for user: " + userDetails.getUsername());
        return ResponseEntity.ok(userDetails);
    }
}
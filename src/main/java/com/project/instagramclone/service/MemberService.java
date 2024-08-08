package com.project.instagramclone.service;

import com.project.instagramclone.dto.SignUpDto;
import com.project.instagramclone.entity.Member;
import com.project.instagramclone.entity.MemberDetail;
import com.project.instagramclone.entity.Sns;
import com.project.instagramclone.exception.DuplicateMemberException;
import com.project.instagramclone.repository.SnsRepository;
import com.project.instagramclone.repository.MemberDetailRepository;
import com.project.instagramclone.repository.MemberRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MemberService {
    private final MemberRepository memberRepository;
    private final MemberDetailRepository memberDetailRepository;
    private final SnsRepository snsRepository;
    private final PasswordEncoder passwordEncoder;

    public MemberService(MemberRepository memberRepository, MemberDetailRepository memberDetailRepository, SnsRepository snsRepository, PasswordEncoder passwordEncoder) {
        this.memberRepository = memberRepository;
        this.memberDetailRepository = memberDetailRepository;
        this.snsRepository = snsRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public SignUpDto signup(SignUpDto signUpDto) {

        if (memberRepository.findByUid(signUpDto.getUid()).orElse(null) != null) {
            throw new DuplicateMemberException("이미 가입되어 있는 유저입니다.");
        }

        // sns_id를 이용해 Sns 엔티티를 조회
        Sns sns = snsRepository.findById(signUpDto.getSns_id())
                .orElseThrow(() -> new IllegalArgumentException("Invalid sns_id: " + signUpDto.getSns_id()));

        Member member = Member.builder()
                .uid(signUpDto.getUid())
                .nickname(signUpDto.getNickname())
                .email(signUpDto.getEmail())
                .sns(sns)
                .activated(true)
                .build();

        memberRepository.save(member);

        MemberDetail memberDetail = MemberDetail.builder()
                .member(member)
                .password(passwordEncoder.encode(signUpDto.getPassword()))
                .build();

        memberDetailRepository.save(memberDetail);

        return SignUpDto.from(member, memberDetail);
    }
}
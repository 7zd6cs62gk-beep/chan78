package com.blog.service;

import com.blog.dto.RegisterForm;
import com.blog.entity.Member;
import com.blog.entity.Role;
import com.blog.repository.MemberRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public MemberService(MemberRepository memberRepository, PasswordEncoder passwordEncoder) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional(readOnly = true)
    public Member findByUsername(String username) {
        return memberRepository.findByUsername(username)
            .orElseThrow(() -> new IllegalArgumentException("회원을 찾을 수 없습니다."));
    }

    @Transactional
    public void register(RegisterForm form) {
        if (!form.getPassword().equals(form.getPasswordConfirm())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
        if (memberRepository.existsByUsername(form.getUsername())) {
            throw new IllegalArgumentException("이미 사용 중인 아이디입니다.");
        }
        if (memberRepository.existsByEmail(form.getEmail())) {
            throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
        }

        Member member = new Member();
        member.setUsername(form.getUsername());
        member.setEmail(form.getEmail());
        member.setPassword(passwordEncoder.encode(form.getPassword()));
        member.setRole(Role.USER);
        member.setCreatedAt(LocalDateTime.now());
        memberRepository.save(member);
    }

    @Transactional
    public void createAdminIfAbsent(String username, String email, String rawPassword) {
        if (memberRepository.existsByUsername(username)) {
            return;
        }
        Member admin = new Member();
        admin.setUsername(username);
        admin.setEmail(email);
        admin.setPassword(passwordEncoder.encode(rawPassword));
        admin.setRole(Role.ADMIN);
        admin.setCreatedAt(LocalDateTime.now());
        memberRepository.save(admin);
    }
}

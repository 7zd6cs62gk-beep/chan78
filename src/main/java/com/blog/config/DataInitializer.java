package com.blog.config;

import com.blog.service.MemberService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final MemberService memberService;

    public DataInitializer(MemberService memberService) {
        this.memberService = memberService;
    }

    @Override
    public void run(String... args) {
        memberService.createAdminIfAbsent("admin", "admin@blog.local", "admin123");
    }
}

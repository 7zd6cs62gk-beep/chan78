package com.blog.controller;

import com.blog.dto.RegisterForm;
import com.blog.service.MemberService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
public class RegisterController {

    private final MemberService memberService;

    public RegisterController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping("/register")
    public String registerForm(Model model) {
        model.addAttribute("form", new RegisterForm());
        return "register";
    }

    @PostMapping("/register")
    public String register(
            @Valid @ModelAttribute("form") RegisterForm form,
            BindingResult bindingResult,
            Model model) {

        if (!bindingResult.hasErrors()) {
            try {
                memberService.register(form);
                return "redirect:/login?registered";
            } catch (IllegalArgumentException e) {
                bindingResult.reject("register.failed", e.getMessage());
            }
        }

        return "register";
    }
}

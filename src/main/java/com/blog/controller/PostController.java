package com.blog.controller;

import com.blog.dto.PostForm;
import com.blog.entity.Member;
import com.blog.service.MemberService;
import com.blog.service.PostService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;
    private final MemberService memberService;

    public PostController(PostService postService, MemberService memberService) {
        this.postService = postService;
        this.memberService = memberService;
    }

    @GetMapping("/new")
    public String newForm(Model model) {
        model.addAttribute("form", new PostForm());
        return "post-form";
    }

    @PostMapping
    public String create(
            @AuthenticationPrincipal UserDetails user,
            @Valid @ModelAttribute("form") PostForm form,
            BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "post-form";
        }

        Member author = memberService.findByUsername(user.getUsername());
        postService.create(author, form);
        return "redirect:/home";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, @AuthenticationPrincipal UserDetails user, Model model) {
        com.blog.entity.Post post = postService.findById(id);
        model.addAttribute("post", post);
        model.addAttribute("isAuthor", post.getAuthor().getUsername().equals(user.getUsername()));
        return "post-detail";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, @AuthenticationPrincipal UserDetails user, Model model) {
        com.blog.entity.Post post = postService.findById(id);
        if (!post.getAuthor().getUsername().equals(user.getUsername())) {
            return "redirect:/posts/" + id;
        }

        PostForm form = new PostForm();
        form.setTitle(post.getTitle());
        form.setContent(post.getContent());
        model.addAttribute("form", form);
        model.addAttribute("postId", id);
        return "post-form";
    }

    @PostMapping("/{id}/edit")
    public String update(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails user,
            @Valid @ModelAttribute("form") PostForm form,
            BindingResult bindingResult,
            Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("postId", id);
            return "post-form";
        }

        try {
            Member current = memberService.findByUsername(user.getUsername());
            postService.update(id, current, form);
            return "redirect:/posts/" + id;
        } catch (IllegalArgumentException e) {
            return "redirect:/posts/" + id;
        }
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, @AuthenticationPrincipal UserDetails user) {
        try {
            Member current = memberService.findByUsername(user.getUsername());
            postService.delete(id, current);
        } catch (IllegalArgumentException ignored) {
            // ignore unauthorized delete
        }
        return "redirect:/home";
    }
}

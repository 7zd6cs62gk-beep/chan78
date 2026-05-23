package com.blog.service;

import com.blog.dto.PostForm;
import com.blog.entity.Member;
import com.blog.entity.Post;
import com.blog.repository.PostRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PostService {

    private final PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @Transactional(readOnly = true)
    public List<Post> findAll() {
        return postRepository.findAllByOrderByCreatedAtDesc();
    }

    @Transactional(readOnly = true)
    public Post findById(Long id) {
        return postRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));
    }

    @Transactional
    public Post create(Member author, PostForm form) {
        Post post = new Post();
        post.setTitle(form.getTitle());
        post.setContent(form.getContent());
        post.setAuthor(author);
        return postRepository.save(post);
    }

    @Transactional
    public void update(Long id, Member currentUser, PostForm form) {
        Post post = findById(id);
        assertAuthor(post, currentUser);
        post.setTitle(form.getTitle());
        post.setContent(form.getContent());
    }

    @Transactional
    public void delete(Long id, Member currentUser) {
        Post post = findById(id);
        assertAuthor(post, currentUser);
        postRepository.delete(post);
    }

    private void assertAuthor(Post post, Member currentUser) {
        if (!post.getAuthor().getId().equals(currentUser.getId())) {
            throw new IllegalArgumentException("수정 권한이 없습니다.");
        }
    }
}

package com.postarc.postarc_backend.posts;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.postarc.postarc_backend.posts.dto.CreatePostRequest;
import com.postarc.postarc_backend.posts.dto.PostResponse;
import com.postarc.postarc_backend.users.model.User;
import com.postarc.postarc_backend.users.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public PostResponse createResponse(Long userId, CreatePostRequest request) {

        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        Post post = Post.builder()
                .user(user)
                .content(request.content())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        Post saved = postRepository.save(post);

        return new PostResponse(saved.getId(), saved.getUser().getUsername(), saved.getContent(), saved.getCreatedAt(),
                saved.getUpdatedAt());
    }

    public List<PostResponse> getAllPosts() {
        return postRepository.findAll()
                .stream()
                .map(p -> new PostResponse(p.getId(), p.getUser().getUsername(), p.getContent(), p.getCreatedAt(),
                        p.getUpdatedAt()))
                .toList();
    }

    public PostResponse getPostById(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        return new PostResponse(post.getId(), post.getUser().getUsername(), post.getContent(), post.getCreatedAt(),
                post.getUpdatedAt());
    }

}

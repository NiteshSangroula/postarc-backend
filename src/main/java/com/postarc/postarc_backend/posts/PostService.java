package com.postarc.postarc_backend.posts;

import java.io.File;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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

    public PostResponse uploadImage(long postId, MultipartFile image) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        if (image.isEmpty()) {
            throw new RuntimeException("Empty file");
        }

        try {
            String uploadDir = System.getProperty("user.dir") + "/uploads/";
            File dir = new File(uploadDir);
            if (!dir.exists())
                dir.mkdirs();

            String fileName = UUID.randomUUID() + "_" + image.getOriginalFilename();
            File dest = new File(uploadDir + fileName);

            // System.out.println("Uploading to: " + new
            // File("uploads/").getAbsolutePath());
            // System.out.println("Received file: " + image.getOriginalFilename());

            image.transferTo(dest);

            post.setImageUrl(fileName);
            postRepository.save(post);

            return new PostResponse(post.getId(),
                    post.getUser().getEmail(),
                    post.getContent(),
                    post.getCreatedAt(),
                    post.getUpdatedAt());

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to upload file");
        }
    }

}

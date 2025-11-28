package com.postarc.postarc_backend.posts;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.postarc.postarc_backend.posts.dto.CreatePostRequest;
import com.postarc.postarc_backend.posts.dto.PostResponse;
import com.postarc.postarc_backend.security.jwt.JwtService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/posts")
public class PostController {
    private final PostService postService;
    private final JwtService jwtService;

    @PostMapping
    public ResponseEntity<PostResponse> createPost(
            @RequestHeader("Authorization") String authHeader,
            @Valid @RequestBody CreatePostRequest request) {

        Long userId = jwtService.extractUserId(authHeader.substring(7));
        return ResponseEntity.status(201).body(postService.createResponse(userId, request));

    }

    @GetMapping
    public ResponseEntity<List<PostResponse>> getAll() {
        return ResponseEntity.ok(postService.getAllPosts());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostResponse> getOne(@PathVariable Long id) {
        return ResponseEntity.ok(postService.getPostById(id));
    }

    @PostMapping("/{postId}/image")
    public ResponseEntity<PostResponse> uploadImage(
            @PathVariable Long postId,
            @RequestParam("file") MultipartFile imageFile) {
        return ResponseEntity.ok(postService.uploadImage(postId, imageFile));
    }

}

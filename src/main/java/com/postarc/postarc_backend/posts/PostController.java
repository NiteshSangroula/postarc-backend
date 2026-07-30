package com.postarc.postarc_backend.posts;

import java.util.List;

import org.springframework.http.HttpStatus;
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

import com.postarc.postarc_backend.common.dto.ApiResponse;
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
  public ResponseEntity<ApiResponse<PostResponse>> createPost(
      @RequestHeader("Authorization") String authHeader,
      @Valid @RequestBody CreatePostRequest request) {

    Long userId = jwtService.extractUserId(authHeader.substring(7));
    PostResponse post = postService.createResponse(userId, request);
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(ApiResponse.success(post, "Post created successfully"));
  }

  @GetMapping
  public ResponseEntity<ApiResponse<List<PostResponse>>> getAll() {
    List<PostResponse> posts = postService.getAllPosts();
    return ResponseEntity.ok(ApiResponse.success(posts, "Posts retrieved successfully"));
  }

  @GetMapping("/{id}")
  public ResponseEntity<ApiResponse<PostResponse>> getOne(@PathVariable Long id) {
    PostResponse post = postService.getPostById(id);
    return ResponseEntity.ok(ApiResponse.success(post, "Post retrieved successfully"));
  }

  @PostMapping("/{postId}/image")
  public ResponseEntity<ApiResponse<PostResponse>> uploadImage(
      @PathVariable Long postId,
      @RequestParam("file") MultipartFile imageFile) {
    PostResponse post = postService.uploadImage(postId, imageFile);
    return ResponseEntity.ok(ApiResponse.success(post, "Image uploaded successfully"));
  }
}

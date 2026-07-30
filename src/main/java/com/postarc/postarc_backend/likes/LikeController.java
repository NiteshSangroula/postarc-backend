package com.postarc.postarc_backend.likes;

import java.security.Principal;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.postarc.postarc_backend.common.dto.ApiResponse;

@RestController
@RequestMapping("/api/v1/posts/{postId}/likes")
public class LikeController {

  private final LikeService likeService;

  public LikeController(LikeService likeService) {
    this.likeService = likeService;
  }

  @PostMapping
  public ResponseEntity<ApiResponse<Void>> like(@PathVariable long postId, Principal principal) {
    likeService.likePost(postId, principal.getName());
    return ResponseEntity.ok(ApiResponse.success("Post liked successfully"));
  }

  @DeleteMapping
  public ResponseEntity<ApiResponse<Void>> unlike(@PathVariable long postId, Principal principal) {
    likeService.unlikePost(postId, principal.getName());
    return ResponseEntity.ok(ApiResponse.success("Post unliked successfully"));
  }

  @GetMapping("/count")
  public ResponseEntity<ApiResponse<Long>> count(@PathVariable long postId) {
    Long count = likeService.countLikes(postId);
    return ResponseEntity.ok(ApiResponse.success(count, "Likes count retrieved successfully"));
  }
}

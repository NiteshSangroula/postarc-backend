package com.postarc.postarc_backend.comments;

import java.security.Principal;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.postarc.postarc_backend.comments.dto.CommentRequest;
import com.postarc.postarc_backend.comments.dto.CommentResponse;
import com.postarc.postarc_backend.common.dto.ApiResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/comments")
@RequiredArgsConstructor
public class CommentController {

  private final CommentService commentService;

  @PostMapping("/{postId}")
  public ResponseEntity<ApiResponse<CommentResponse>> addComment(
      @PathVariable long postId,
      @RequestBody CommentRequest request,
      Principal principal) {
    CommentResponse comment = commentService.addComment(postId, request, principal.getName());
    return ResponseEntity.ok(ApiResponse.success(comment, "Comment added successfully"));
  }

  @GetMapping("/{postId}")
  public ResponseEntity<ApiResponse<List<CommentResponse>>> getComments(@PathVariable long postId) {
    List<CommentResponse> comments = commentService.getComments(postId);
    return ResponseEntity.ok(ApiResponse.success(comments, "Comments retrieved successfully"));
  }
}

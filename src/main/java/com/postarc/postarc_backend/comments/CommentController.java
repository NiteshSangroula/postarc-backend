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

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/comments/")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/{postId}")
    public ResponseEntity<CommentResponse> addComment(
            @PathVariable long postId,
            @RequestBody CommentRequest request,
            Principal principal) {
        return ResponseEntity.ok(commentService.addComment(postId, request, principal.getName()));
    }

    @GetMapping("/{postId}")
    public ResponseEntity<List<CommentResponse>> getComments(@PathVariable long postId) {
        return ResponseEntity.ok(commentService.getComments(postId));
    }

}

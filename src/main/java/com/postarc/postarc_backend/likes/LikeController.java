package com.postarc.postarc_backend.likes;

import java.security.Principal;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/posts/{postId}/likes")
public class LikeController {

    private final LikeService likeService;

    public LikeController(LikeService likeService) {
        this.likeService = likeService;
    }

    @PostMapping
    public ResponseEntity<?> like(@PathVariable long postId, Principal principal) {
        likeService.likePost(postId, principal.getName());
        return ResponseEntity.ok("Liked");
    }

    @DeleteMapping
    public ResponseEntity<?> unlike(@PathVariable long postId, Principal principal) {
        likeService.unlikePost(postId, principal.getName());
        return ResponseEntity.ok("Unliked");
    }

    @GetMapping("/count")
    public ResponseEntity<Long> count(@PathVariable long postId) {
        return ResponseEntity.ok(likeService.countLikes(postId));
    }

}

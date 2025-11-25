package com.postarc.postarc_backend.comments;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.postarc.postarc_backend.comments.dto.CommentRequest;
import com.postarc.postarc_backend.comments.dto.CommentResponse;
import com.postarc.postarc_backend.posts.Post;
import com.postarc.postarc_backend.posts.PostRepository;
import com.postarc.postarc_backend.users.model.User;
import com.postarc.postarc_backend.users.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    public CommentResponse addComment(Long postId, CommentRequest request, String username) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found!"));

        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new RuntimeException("User not found!"));

        Comment comment = Comment.builder()
                .content(request.content())
                .createdAt(LocalDateTime.now())
                .post(post)
                .user(user)
                .build();

        commentRepository.save(comment);

        return new CommentResponse(
                comment.getId(),
                comment.getContent(),
                user.getEmail(),
                comment.getCreatedAt());
    }

    public List<CommentResponse> getComments(long postId) {
        List<Comment> comments = commentRepository.findByPostId(postId);

        return comments.stream()
                .map(c -> new CommentResponse(
                        c.getId(),
                        c.getContent(),
                        c.getUser().getEmail(),
                        c.getCreatedAt()))
                .toList();

    }

}

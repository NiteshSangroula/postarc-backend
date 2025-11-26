package com.postarc.postarc_backend.likes;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.postarc.postarc_backend.posts.Post;
import com.postarc.postarc_backend.users.model.User;

public interface LikeRepository extends JpaRepository<Like, Long> {
    Optional<Like> findByUserAndPost(User user, Post post);

    long countByPost(Post post);
}

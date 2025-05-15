package com.kidou.comments_api.repository;

import com.kidou.comments_api.model.Like;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {
    boolean existsByCommentIdAndUserId(Long commentId, Long userId);

    List<Like> findAllByUserId(Long userId);

   Optional<Like> findByCommentIdAndUserId (Long commentId, Long userId);
}

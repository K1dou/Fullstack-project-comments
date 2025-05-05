package com.kidou.comments_api.service;

import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.kidou.comments_api.model.Comment;
import com.kidou.comments_api.repository.CommentRepository;

@Service
public class LikeSyncService {

    private final RedisLikeService redisLikeService;
    private final CommentRepository commentRepository;

    public LikeSyncService(RedisLikeService redisLikeService, CommentRepository commentRepository) {
        this.redisLikeService = redisLikeService;
        this.commentRepository = commentRepository;
    }

    @Scheduled(fixedRate = 60000)
    public void syncLikes() {
        List<Comment> comments = commentRepository.findAll();

        for (Comment comment : comments) {
            Long commentId = comment.getId();
            long redisLikeCount = redisLikeService.getLikeCount(commentId);

            Integer likeCount = comment.getLikeCount();
            int safeLikeCount = likeCount != null ? likeCount : 0;

            if (safeLikeCount != redisLikeCount) {
                comment.setLikeCount((int) redisLikeCount);
                commentRepository.save(comment);
                System.out.println("Sincronizado comentário ID " + commentId + ": " + redisLikeCount + " likes.");
            }
        }
    }
}

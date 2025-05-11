package com.kidou.comments_api.service;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import com.kidou.comments_api.exceptions.BusinessException;

@Service
public class RedisLikeService {

    private final StringRedisTemplate redisTemplate;

    public RedisLikeService(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    private String likeKey(Long commentId) {
        return "comment:" + commentId + ":likes";
    }

    private String likedByKey(Long commentId) {
        return "comment:" + commentId + ":likedBy";
    }

    public void likePost(Long commentId, Long userId) {
        String userIdStr = userId.toString();
        String likedByKey = likedByKey(commentId);

        Boolean alreadyLiked = redisTemplate.opsForSet().isMember(likedByKey, userIdStr);
        if (Boolean.TRUE.equals(alreadyLiked)) {
            throw new BusinessException("Usuário já curtiu esse comentário");
        }

        redisTemplate.opsForSet().add(likedByKey, userIdStr);
        redisTemplate.opsForValue().increment(likeKey(commentId));
    }

    public void unlikePost(Long commentId, Long userId) {
        String userIdStr = userId.toString();
        String likedByKey = likedByKey(commentId);

        Boolean liked = redisTemplate.opsForSet().isMember(likedByKey, userIdStr);
        if (Boolean.TRUE.equals(liked)) {
            redisTemplate.opsForSet().remove(likedByKey, userIdStr);
            redisTemplate.opsForValue().decrement(likeKey(commentId));
        }
    }

    public void resetLikeCount(Long commentId) {
        redisTemplate.delete(likeKey(commentId));
        redisTemplate.delete(likedByKey(commentId));
    }

    public int getLikeCount(Long commentId) {
        String value = redisTemplate.opsForValue().get(likeKey(commentId));
        return (int) (value != null ? Long.parseLong(value) : 0);
    }

}
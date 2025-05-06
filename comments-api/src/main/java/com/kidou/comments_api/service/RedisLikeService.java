package com.kidou.comments_api.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RedisLikeService {

    private final StringRedisTemplate redisTemplate;

    public RedisLikeService(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    private String getKey(Long commentId) {
        return "comment:" + commentId + ":likes";
    }

    public void likePost(Long commentId) {
        redisTemplate.opsForValue().increment(getKey(commentId));
    }

    public int getLikeCount(Long commentId) {
        String value = redisTemplate.opsForValue().get(getKey(commentId));
        return (int) (value != null ? Long.parseLong(value) : 0);
    }

}
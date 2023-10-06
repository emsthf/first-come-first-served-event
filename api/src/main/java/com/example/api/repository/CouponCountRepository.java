package com.example.api.repository;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class CouponCountRepository {

    private final RedisTemplate<String, String> redisTemplate;  // RedisTemplate: Redis 명령어를 실행하게 해줌

    public CouponCountRepository(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }


    public Long increment() {
        return redisTemplate.opsForValue().increment("coupon_count");
    }
}

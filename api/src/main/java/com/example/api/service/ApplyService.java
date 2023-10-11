package com.example.api.service;

import com.example.api.producer.CouponCreateProducer;
import com.example.api.repository.AppliedUserRepository;
import com.example.api.repository.CouponCountRepository;
import com.example.api.repository.CouponRepository;
import org.springframework.stereotype.Service;

@Service
public class ApplyService {

    private final CouponRepository couponRepository;
    private final CouponCountRepository couponCountRepository;
    private final CouponCreateProducer couponCreateProducer;
    private final AppliedUserRepository appliedUserRepository;

    public ApplyService(
            CouponRepository couponRepository,
            CouponCountRepository couponCountRepository,
            CouponCreateProducer couponCreateProducer,
            AppliedUserRepository appliedUserRepository
    ) {
        this.couponRepository = couponRepository;
        this.couponCountRepository = couponCountRepository;
        this.couponCreateProducer = couponCreateProducer;
        this.appliedUserRepository = appliedUserRepository;
    }

    /**
     * 쿠폰 발급
     */
    public void apply(Long userId) {
        Long apply = appliedUserRepository.add(userId);  // redis Set에 userId 추가

        // 이미 발급된 유저는 발급X
        if (apply != 1) {
            return;
        }

        Long count = couponCountRepository.increment();  // jpa로 테이블에서 count 하던 것을 redis로 변경

        // 발급 가능 개수 초과시 발급X
        if (count > 100) {
            return;
        }

        couponCreateProducer.create(userId);  // 카프카로 메시지 전송
    }
}

package com.example.api.service;

import com.example.api.repository.CouponRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class ApplyServiceTest {

    @Autowired
    private ApplyService applyService;

    @Autowired
    private CouponRepository couponRepository;


    @Test
    void 한번만_응모() throws Exception {
        // given

        // when
        applyService.apply(1L);

        // then
        long count = couponRepository.count();
        assertThat(count).isEqualTo(1);
    }

    @Test
    void 여러_명_응모() throws Exception {
        // given
        int threadCount = 1000;
        ExecutorService executorService = Executors.newFixedThreadPool(32);  // 병렬 작업을 간단하게 할 수 있게 해주는 자바 유틸리티 클래스
        CountDownLatch latch = new CountDownLatch(threadCount);  // CountDownLatch: 스레드가 실행되기 전에 특정 작업이 끝나기를 기다리는 기능을 제공하는 클래스

        // when
        for (int i = 0; i < threadCount; i++) {
            long userId = i;
            executorService.submit(() -> {
                try {
                    applyService.apply(userId);
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        Thread.sleep(10000);  // 테스트 케이스 종료 전까지 consumer 애플리케이션의 쿠폰 발급을 모두 처리하기 위한 10초 스레드 슬립

        // then
        long count = couponRepository.count();
        assertThat(count).isEqualTo(100);
    }
}
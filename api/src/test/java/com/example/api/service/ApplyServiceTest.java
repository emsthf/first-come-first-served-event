package com.example.api.service;

import com.example.api.repository.CouponRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

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
}
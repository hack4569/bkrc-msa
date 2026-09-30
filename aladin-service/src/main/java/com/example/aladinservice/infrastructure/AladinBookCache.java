package com.example.aladinservice.infrastructure;

import com.example.aladinservice.application.response.AladinBookPageResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.time.Duration;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class AladinBookCache {
    private static final String CACHE_KEY = "aladin:books:all";
    private static final Duration TTL = Duration.ofHours(24);

    private final StringRedisTemplate redisTemplate;
    private final ObjectMapper objectMapper;

    public Optional<AladinBookPageResponse> find() {
        try {
            String cached = redisTemplate.opsForValue().get(CACHE_KEY);
            return StringUtils.hasText(cached)
                    ? Optional.of(objectMapper.readValue(cached, AladinBookPageResponse.class))
                    : Optional.empty();
        } catch (Exception exception) {
            log.warn("[알라딘] Redis 조회 실패. DB로 대체합니다.", exception);
            return Optional.empty();
        }
    }

    public void save(AladinBookPageResponse response) {
        try {
            redisTemplate.opsForValue().set(CACHE_KEY, objectMapper.writeValueAsString(response), TTL);
        } catch (Exception exception) {
            log.warn("[알라딘] Redis 저장 실패. 응답은 정상 반환합니다.", exception);
        }
    }

    public void evict() {
        try {
            redisTemplate.delete(CACHE_KEY);
        } catch (Exception exception) {
            log.warn("[알라딘] Redis 캐시 삭제 실패", exception);
        }
    }
}

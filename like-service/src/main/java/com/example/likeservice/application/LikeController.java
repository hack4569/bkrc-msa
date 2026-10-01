package com.example.likeservice.application;

import com.example.common.security.GatewayMemberAuthenticationFilter;
import com.example.likeservice.application.response.LikeCountResponse;
import com.example.likeservice.application.response.LikeResponse;
import com.example.likeservice.application.response.MyLikeResponse;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/like-service/v1/like")
public class LikeController {
    private final LikeService likeService;

    @PostMapping("/{itemId}")
    public LikeResponse like(
            @RequestHeader(value = GatewayMemberAuthenticationFilter.MEMBER_ID_HEADER, required = false) Long memberId,
            @PathVariable @Positive Integer itemId) {
        return likeService.like(itemId, memberId);
    }

    @PostMapping("/cancel/{itemId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void unlike(
            @RequestHeader(GatewayMemberAuthenticationFilter.MEMBER_ID_HEADER) Long memberId,
            @PathVariable @Positive Integer itemId) {
        likeService.unlike(itemId, memberId);
    }

    @GetMapping("/me")
    public List<MyLikeResponse> findMine(
            @RequestHeader(GatewayMemberAuthenticationFilter.MEMBER_ID_HEADER) Long memberId) {
        return likeService.findMine(memberId);
    }

    @GetMapping("/count/{itemId}")
    public LikeCountResponse count(@PathVariable @Positive Integer itemId) {
        return likeService.count(itemId);
    }
}

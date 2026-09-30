package com.example.likeservice;

import com.example.common.security.GatewayMemberAuthenticationFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/like-service")
@Slf4j
public class LikeController {

    @GetMapping("/welcome")
    public String welcome(
            @RequestHeader(GatewayMemberAuthenticationFilter.MEMBER_ID_HEADER) Long memberId) {
        log.debug("Authenticated memberId={}", memberId);
        return "Welcome to the Like service.";
    }
}

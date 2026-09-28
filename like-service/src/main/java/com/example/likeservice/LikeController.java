package com.example.likeservice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/like-service")
@Slf4j
public class LikeController {

    @GetMapping("/welcome")
    public String welcome() {
        return "Welcome to the Like service.";
    }
}

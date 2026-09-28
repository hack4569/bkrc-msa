package com.example.userservice.application;

import com.example.userservice.application.request.LoginForm;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {

    @PostMapping("/login")
    public void login(@RequestBody LoginForm loginForm) {
        // Spring Security AuthenticationFilter가 처리
    }
}

package com.example.userservice.application.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class LoginForm {
        @NotEmpty
    private String loginId;

        @NotEmpty
    private String password;

        private boolean autoLogin;
}

package com.dyongs.demo.domain.user.controller;

import com.dyongs.demo.domain.user.dto.LoginRequest;
import com.dyongs.demo.domain.user.dto.LoginResponse;
import com.dyongs.demo.domain.user.dto.SignUpRequest;
import com.dyongs.demo.domain.user.dto.UserResponse;
import com.dyongs.demo.domain.user.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    // 회원가입
    @PostMapping("/signup")
    public UserResponse signUp(@Valid @RequestBody SignUpRequest request) {
        return authService.signUp(request);
    }

    // 로그인
    @PostMapping("/login")
    public LoginResponse login(@Valid @RequestBody LoginRequest request) {
        return authService.login(request);
    }
}

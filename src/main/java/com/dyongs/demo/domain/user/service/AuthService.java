package com.dyongs.demo.domain.user.service;

import com.dyongs.demo.domain.user.dto.LoginRequest;
import com.dyongs.demo.domain.user.dto.LoginResponse;
import com.dyongs.demo.domain.user.dto.SignUpRequest;
import com.dyongs.demo.domain.user.dto.UserResponse;
import com.dyongs.demo.domain.user.entity.User;
import com.dyongs.demo.domain.user.repository.UserRepository;
import com.dyongs.demo.global.exception.EmailAlreadyExistsException;
import com.dyongs.demo.global.exception.InvalidCredentialsException;
import com.dyongs.demo.global.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    // 회원가입
    public UserResponse signUp(SignUpRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyExistsException(request.getEmail());
        }

        String encodedPassword = passwordEncoder.encode(request.getPassword());

        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(encodedPassword)
                .build();

        User saved = userRepository.save(user);

        log.info("User signed up. id={}, email={}", saved.getId(), saved.getEmail());

        return new UserResponse(saved);
    }

    // 로그인 + JWT 발급
    public LoginResponse login(LoginRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(InvalidCredentialsException::new);

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException();
        }

        String accessToken = jwtTokenProvider.generateAccessToken(user);

        log.info("User login success. id={}, email={}", user.getId(), user.getEmail());

        return LoginResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .accessToken(accessToken)
                .build();
    }
}

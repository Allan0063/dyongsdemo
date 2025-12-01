package com.dyongs.demo.domain.user.service;

import com.dyongs.demo.domain.user.dto.LoginRequest;
import com.dyongs.demo.domain.user.dto.SignUpRequest;
import com.dyongs.demo.domain.user.dto.UserResponse;
import com.dyongs.demo.domain.user.entity.User;
import com.dyongs.demo.domain.user.repository.UserRepository;
import com.dyongs.demo.global.exception.EmailAlreadyExistsException;
import com.dyongs.demo.global.exception.InvalidCredentialsException;
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

    // 회원가입
    public UserResponse signUp(SignUpRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyExistsException(request.getEmail());
        }

        String encodedPassword = passwordEncoder.encode(request.getPassword());
        // log.info("encodedPassword={}", encodedPassword);  // ❌ 실제로는 비번 로그 찍지 말기

        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(encodedPassword)
                .build();

        User saved = userRepository.save(user);

        log.info("User signed up. id={}, email={}", saved.getId(), saved.getEmail());

        return new UserResponse(saved);
    }

    // 로그인
    public UserResponse login(LoginRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(InvalidCredentialsException::new);

        // raw password와 encoded password 비교
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException();
        }

        log.info("User login success. id={}, email={}", user.getId(), user.getEmail());

        return new UserResponse(user);
    }
}

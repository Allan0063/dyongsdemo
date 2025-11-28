package com.dyongs.demo.domain.user.service;

import com.dyongs.demo.domain.user.dto.UserRequest;
import com.dyongs.demo.domain.user.dto.UserResponse;
import com.dyongs.demo.domain.user.entity.User;
import com.dyongs.demo.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    // Create
    public UserResponse createUser(UserRequest request) {
        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .build();

        User saved = userRepository.save(user);
        return new UserResponse(saved);
    }

    // Read (단건 조회)
    public UserResponse getUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User Not Found"));

        return new UserResponse(user);
    }
}

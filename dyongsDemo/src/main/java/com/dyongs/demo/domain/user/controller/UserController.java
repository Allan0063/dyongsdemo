package com.dyongs.demo.domain.user.controller;

import com.dyongs.demo.domain.user.dto.UserRequest;
import com.dyongs.demo.domain.user.dto.UserResponse;
import com.dyongs.demo.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    // Create
    @PostMapping
    public UserResponse create(@RequestBody UserRequest request) {
        return userService.createUser(request);
    }

    // Read
    @GetMapping("/{id}")
    public UserResponse getOne(@PathVariable Long id) {
        return userService.getUser(id);
    }
}

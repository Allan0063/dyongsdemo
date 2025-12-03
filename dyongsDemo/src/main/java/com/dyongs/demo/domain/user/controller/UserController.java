package com.dyongs.demo.domain.user.controller;

import com.dyongs.demo.domain.user.dto.UserRequest;
import com.dyongs.demo.domain.user.dto.UserResponse;
import com.dyongs.demo.domain.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    // Read
    @GetMapping("/{id}")
    public UserResponse getOne(@PathVariable Long id) {
        return userService.getUser(id);
    }

    // Read - 전체 목록
    @GetMapping
    public List<UserResponse> getAll() {
        return userService.getUsers();
    }

    // Create
    @PostMapping
    public UserResponse create(@Valid @RequestBody UserRequest request) {
        return userService.createUser(request);
    }

    // Update
    @PutMapping("/{id}")
    public UserResponse update(
            @PathVariable Long id,
            @Valid @RequestBody UserRequest request
    ) {
        return userService.updateUser(id, request);
    }

    // Delete
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        userService.deleteUser(id);
    }
}

package com.dyongs.demo.domain.user.dto;

import com.dyongs.demo.domain.user.entity.User;
import lombok.Getter;

@Getter
public class UserResponse {

    private Long id;
    private String name;
    private String email;

    public UserResponse(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.email = user.getEmail();
    }
}

package com.dyongs.demo.domain.user.exception;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(Long id) {
        super("User not found. id=" + id);
    }
}

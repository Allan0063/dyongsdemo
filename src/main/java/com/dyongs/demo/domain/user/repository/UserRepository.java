package com.dyongs.demo.domain.user.repository;

import com.dyongs.demo.domain.user.entity.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(@NotBlank(message = "이메일은 필수 값입니다.") @Email(message = "올바른 이메일 형식이 아닙니다.") String email);
    Optional<User> findByEmail(String email);
}
package com.dyongs.demo.domain.user.repository;

import com.dyongs.demo.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
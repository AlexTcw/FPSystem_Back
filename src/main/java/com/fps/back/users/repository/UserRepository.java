package com.fps.back.users.repository;

import com.fps.back.users.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    User findUserByUsername(String username);
    boolean existsUserByUsername(String username);

    User findUserByUserId(Long userId);
}

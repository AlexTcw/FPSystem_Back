package com.fps.back.entry.repository;

import com.fps.back.entry.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserEntryRepository extends JpaRepository<User, Long> {
    User findUserByUserId(Long userId);

    boolean existsUserByUserId(Long userId);
}

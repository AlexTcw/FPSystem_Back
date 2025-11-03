package com.fps.back.users.repository;

import com.fps.back.users.model.dto.Generic.UserDTO;
import com.fps.back.users.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Long> {

    User findUserByUsername(String username);
    boolean existsUserByUsername(String username);

    @Query("""
            select new com.fps.back.users.model.dto.UserDTO(u.username,u.email,u.firstName,u.lastName)
            from User u
            join u.roles ur
            join ur.permissions rp
            where u.userId = :user_id
            """)
    UserDTO findUserByUserId(@Param("user_id") Long userID);
}

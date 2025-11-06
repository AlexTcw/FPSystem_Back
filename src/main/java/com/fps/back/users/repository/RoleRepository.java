package com.fps.back.users.repository;

import com.fps.back.users.model.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Set<Role> findRoleByRoleId(Long roleId);

    boolean existsRoleByRoleId(Long roleId);
}

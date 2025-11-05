package com.fps.back.users.model.entity;

import com.fps.back.users.model.enums.RoleEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "role",schema = "fps")
public class Role implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "role_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roleId;
    @Enumerated(EnumType.STRING)
    @Column(name = "name",nullable = false,unique = true,updatable = false)
    private RoleEnum name;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "rol_permission",
            joinColumns = @JoinColumn(name = "role_id"),
            inverseJoinColumns = @JoinColumn(name = "permission_id"),
            schema = "fps"
    )
    private Set<Permission> permissions = new HashSet<>();

}

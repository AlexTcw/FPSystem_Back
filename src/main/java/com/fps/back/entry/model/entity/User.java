package com.fps.back.entry.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "User_entry")
@Table(schema = "fps", name = "usuarios")
public class User implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;
    @Column(name = "phone")
    private String phone;
    @Column(name = "address")
    private String address;
    @Column(name = "email", nullable = false, unique = true)
    private String email;
    @Column(name = "username", nullable = false, unique = true)
    private String username;
    @Column(name = "password", nullable = false)
    private String password;
    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<Fingerprint> fingerprints;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_schedule",
            joinColumns = @JoinColumn(name = "user_id"), // Asegúrate de usar el nombre real en la tabla
            inverseJoinColumns = @JoinColumn(name = "schedule_id"),
            schema = "fps"
    )
    private Set<Schedule> schedules;


}

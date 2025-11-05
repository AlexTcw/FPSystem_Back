package com.fps.back.entry.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.Set;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "fingerprint",schema = "fps")
public class Fingerprint implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "fingerprint_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long fingerprintId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User usuario;
    @Column(name = "created_at", columnDefinition = "TIMESTAMP WITH TIME ZONE DEFAULT NOW()")
    private OffsetDateTime createdAt = OffsetDateTime.now();
    @Column(name = "updated_at", columnDefinition = "TIMESTAMP WITH TIME ZONE DEFAULT NOW()")
    private OffsetDateTime updatedAt = OffsetDateTime.now();
    @Column(name = "inactive_at")
    private OffsetDateTime inactiveAt;
    @Column(name = "is_active")
    private Boolean isActive = true;
    @Column(name = "device_id", nullable = false)
    private Integer deviceId;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "record_fingerprint",
            joinColumns = @JoinColumn(name = "fingerprint_id"),
            inverseJoinColumns = @JoinColumn(name = "record_id"),
            schema = "fps"
    )
    private Set<Records> records;
}

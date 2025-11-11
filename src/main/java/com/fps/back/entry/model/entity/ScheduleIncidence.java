package com.fps.back.entry.model.entity;

import com.fps.back.entry.model.enums.ScheduleTypeEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@Entity()
@AllArgsConstructor
@NoArgsConstructor
@Table(schema = "fps", name = "schedule_incidence")
public class ScheduleIncidence implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "schedule_incidence_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long scheduleIncidenceId;
    @Enumerated(EnumType.STRING)
    @Column(name = "incidence_type")
    private ScheduleTypeEnum incidenceType;
    @Column(name = "justified")
    private Boolean justified;
    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt = OffsetDateTime.now();
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "schedule_id", nullable = false)
    private Schedule schedule;

}

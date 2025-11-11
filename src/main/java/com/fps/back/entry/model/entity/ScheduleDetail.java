package com.fps.back.entry.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@Builder
@Entity()
@AllArgsConstructor
@NoArgsConstructor
@Table(schema = "fps", name = "schedule_detail")
public class ScheduleDetail implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "schedule_detail_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long scheduleDetailId;
    @Column(name = "day_of_week", nullable = false)
    private Integer dayOfWeek;
    @Column(name = "entry_time", nullable = false)
    private LocalTime entryTime;
    @Column(name = "exit_time", nullable = false)
    private LocalTime exitTime;
    @Column(name = "is_active")
    private Boolean isActive = true;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "schedule_id", nullable = false)
    private Schedule schedule;
}

package com.fps.back.entry.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "schedule", schema = "fps")
public class Schedule implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "schedule_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long scheduleId;
    @Column(name = "day_of_week", nullable = false)
    private Integer dayOfWeek;
    @Column(name = "entry_time", nullable = false)
    private LocalTime entryTime;
    @Column(name = "exit_time", nullable = false)
    private LocalTime exitTime;
    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;
    @Column(name = "end_date")
    private LocalDate endDate;
    @Column(name = "is_active")
    private Boolean isActive = true;
}

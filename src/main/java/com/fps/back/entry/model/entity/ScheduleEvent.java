package com.fps.back.entry.model.entity;

import com.fps.back.entry.model.enums.ScheduleTypeEnum;
import com.fps.back.entry.model.enums.TypeRecordEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.OffsetDateTime;

@Data
@Builder
@Entity()
@AllArgsConstructor
@NoArgsConstructor
@Table(schema = "fps", name = "schedule_event")
public class ScheduleEvent implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "schedule_event_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long schedule_event_id;
    @Enumerated(EnumType.STRING)
    @Column(name = "event_type")
    private ScheduleTypeEnum eventType;
    private TypeRecordEnum incidenceType;
    @Column(name = "justified")
    private Boolean justified = false;
    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt = OffsetDateTime.now();
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "schedule_id", nullable = false)
    private Schedule schedule;
}

package com.fps.back.entry.repository;

import com.fps.back.entry.model.dto.response.ResponseJsonIncidence;
import com.fps.back.entry.model.entity.ScheduleEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Set;

public interface ScheduleEventRepository extends JpaRepository<ScheduleEvent, Long> {

    @Query("""
    SELECT new com.fps.back.entry.model.dto.response.ResponseJsonIncidence(
        U.userId,
        S.scheduleId,
        E.schedule_event_id,
        E.eventType,
        E.justified,
        E.createdAt
    )
    FROM User_entry U
    JOIN U.schedules S
    JOIN S.events E
    WHERE S.scheduleId = :schedule_id
      AND E.eventType IN ('DELAY', 'ABSENCE', 'PERMISSION', 'OTHER')
      AND E.createdAt BETWEEN :start_date AND :end_date
""")
    Set<ResponseJsonIncidence> getIncidenceByScheduleIdAndDate(
            @Param("schedule_id") Long scheduleID,
            @Param("start_date") LocalDateTime startDate,
            @Param("end_date") LocalDateTime endDate
    );

}

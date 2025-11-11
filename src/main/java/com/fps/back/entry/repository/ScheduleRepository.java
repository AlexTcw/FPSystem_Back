package com.fps.back.entry.repository;

import com.fps.back.entry.model.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ScheduleRepository extends JpaRepository<Schedule,Long> {

    @Query("""
    SELECT CASE WHEN COUNT(s) > 0 THEN TRUE ELSE FALSE END
    FROM User_entry u
    JOIN u.schedules s
    WHERE s.isActive = TRUE
      AND u.userId = :userID
    """)
    boolean existActiveScheduleForUser(@Param("userID") Long userID);

    Schedule findScheduleByScheduleId(Long scheduleId);
}

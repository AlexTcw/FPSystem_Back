package com.fps.back.entry.repository;

import com.fps.back.entry.model.dto.dto.DayScheduleDTO;
import com.fps.back.entry.model.dto.dto.UserSchedule;
import com.fps.back.entry.model.dto.response.ResponseJsonUserScheduleDetail;
import com.fps.back.entry.model.entity.Schedule;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;

public interface ScheduleRepository extends JpaRepository<Schedule,Long> {

    @Query("""
        SELECT CASE WHEN COUNT(s) > 0 THEN TRUE ELSE FALSE END
        FROM User_entry u
        JOIN u.schedules s
        WHERE s.isActive = TRUE
          AND u.userId = :userID
    """)
    boolean existActiveScheduleForUser(@Param("userID") Long userID);

    @Query("""
            select s
            FROM User_entry u
            JOIN u.schedules s
            WHERE s.isActive = TRUE
            AND u.userId = :userID
            """)
    Schedule findActiveScheduleForUser(@Param("userID") Long userID);

    Schedule findScheduleByScheduleId(Long scheduleId);

    @Query("""
            SELECT new com.fps.back.entry.model.dto.dto.UserSchedule(
                        U.userId, S.scheduleId, CONCAT(U.firstName, ' ', U.lastName),
                        S.isActive, S.scheduleType
                        )
            FROM User_entry U
            JOIN U.schedules S
            WHERE :keyword IS NULL
            OR :keyword = ''
            OR CAST(U.userId AS string) = :keyword
            OR UPPER(translate(U.firstName, 'ñáéíóúàèìòùãõâêîôûäëïöüç', 'naeiouaeiouaeiouc'))
               LIKE CONCAT('%', UPPER(translate(REPLACE(:keyword, '_', ''), 'ñáéíóúàèìòùãõâêîôûäëïöüç', 'naeiouaeiouaeiouc')), '%')
            OR UPPER(translate(U.email, 'ñáéíóúàèìòùãõâêîôûäëïöüç', 'naeiouaeiouaeiouc'))
               LIKE CONCAT('%', UPPER(translate(REPLACE(:keyword, '_', ''), 'ñáéíóúàèìòùãõâêîôûäëïöüç', 'naeiouaeiouaeiouc')), '%')
            OR UPPER(translate(S.scheduleType, 'ñáéíóúàèìòùãõâêîôûäëïöüç', 'naeiouaeiouaeiouc'))
               LIKE CONCAT('%', UPPER(translate(REPLACE(:keyword, '_', ''), 'ñáéíóúàèìòùãõâêîôûäëïöüç', 'naeiouaeiouaeiouc')), '%')
            ORDER BY S.scheduleId, S.scheduleType DESC
            """)
    Page<UserSchedule> findAllSchedule(Pageable pageable, @Param("keyword")String keyword);


    @Query("""
    SELECT new com.fps.back.entry.model.dto.response.ResponseJsonUserScheduleDetail(
        U.userId,
        CONCAT(U.firstName, ' ', U.lastName),
        U.email,
        U.username,
        S.scheduleId,
        S.createdAt,
        S.endDate,
        S.isActive,
        S.scheduleType,
        null,
        COALESCE(
            (SELECT R.type
             FROM Fingerprint F
             JOIN F.records R
             WHERE F.usuario.userId = U.userId
             ORDER BY R.recordId DESC
             LIMIT 1),
            'UNKNOWN'
        )
    )
    FROM User_entry U
    JOIN U.schedules S
    WHERE S.scheduleId = :scheduleID
    """)
    ResponseJsonUserScheduleDetail findUserScheduleDetailByScheduleId(@Param("scheduleID") Long scheduleId);

    @Query("""
            SELECT new com.fps.back.entry.model.dto.dto.DayScheduleDTO(
                        D.dayOfWeek, D.entryTime,D.exitTime
                        )
            FROM ScheduleDetail D
            WHERE D.schedule.scheduleId = :scheduleId
            """)
    Set<DayScheduleDTO> findDetailsByScheduleId(@Param("scheduleId") Long scheduleId);

    boolean existsScheduleByScheduleId(Long scheduleId);
}

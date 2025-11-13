package com.fps.back.entry.service.attendance;

import com.fps.back.entry.model.dto.response.ResponseJsonAttendance;
import com.fps.back.entry.model.dto.response.ResponseJsonIncidence;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Set;

public interface AttendanceService {
    Set<ResponseJsonIncidence> getIncidenceByScheduleIdAndDate(Long scheduleID, LocalDateTime startDate, LocalDateTime endDate);

    @Transactional(readOnly = true)
    Page<ResponseJsonAttendance> getAttendancePage(LocalDateTime startDate, LocalDateTime endDate,
                                                   String keyword, int page, int pageSize);

    @Transactional
    void createAttendance();
}

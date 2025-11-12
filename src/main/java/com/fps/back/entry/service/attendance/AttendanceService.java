package com.fps.back.entry.service.attendance;

import com.fps.back.entry.model.dto.response.ResponseJsonIncidence;

import java.time.LocalDateTime;
import java.util.Set;

public interface AttendanceService {
    Set<ResponseJsonIncidence> getIncidenceByScheduleIdAndDate(Long scheduleID, LocalDateTime startDate, LocalDateTime endDate);
}

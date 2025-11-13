package com.fps.back.entry.model.dto.response;

import com.fps.back.entry.model.enums.TypeRecordEnum;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

public record ResponseJsonAttendance(Long userID,Long ScheduleID, Long attendanceID,
                                     String name, String email, String username,
                                     OffsetDateTime attendanceDate) {
}

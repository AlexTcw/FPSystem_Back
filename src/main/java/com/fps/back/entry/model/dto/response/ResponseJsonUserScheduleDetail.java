package com.fps.back.entry.model.dto.response;

import com.fps.back.entry.model.dto.dto.DayScheduleDTO;
import com.fps.back.entry.model.enums.ScheduleTypeEnum;
import com.fps.back.entry.model.enums.TypeRecordEnum;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.Set;

public record ResponseJsonUserScheduleDetail(Long userId, String name, String email, String username, Long scheduleId,
                                             LocalDateTime createdAt, LocalDateTime endDate, Boolean isActive,
                                             ScheduleTypeEnum scheduleType, Set<DayScheduleDTO> daysOfWeek,
                                             TypeRecordEnum status) {
}

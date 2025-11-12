package com.fps.back.entry.model.dto.dto;

import com.fps.back.entry.model.enums.ScheduleTypeEnum;

public record UserSchedule(Long userID, Long scheduleId, String name, Boolean isActive, ScheduleTypeEnum schedule_type) {
}

package com.fps.back.entry.model.dto.consume;

import com.fps.back.entry.model.dto.dto.DayScheduleDTO;
import com.fps.back.entry.model.enums.ScheduleTypeEnum;

import java.time.LocalDateTime;
import java.util.Set;

public record ConsumeJsonSchedule(Long userID, Set<DayScheduleDTO> daysOfWeek, LocalDateTime startDate,
                                  ScheduleTypeEnum schedule_type, Boolean isActive) {
}

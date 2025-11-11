package com.fps.back.entry.model.dto.consume;

import com.fps.back.entry.model.dto.dto.DayScheduleDTO;

import java.time.LocalDateTime;
import java.util.Set;

public record ConsumeJsonSchedule(Long userID, Set<DayScheduleDTO> daysOfWeek, LocalDateTime startDate,
                                  String schedule_type, Boolean isActive) {
}

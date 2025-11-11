package com.fps.back.entry.model.dto.dto;

import java.time.LocalDateTime;
import java.time.LocalTime;

public record DayScheduleDTO(int day_of_week, LocalTime entry_time, LocalTime exit_time) {
}

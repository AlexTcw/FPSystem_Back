package com.fps.back.entry.model.dto.consume;

import java.time.LocalDateTime;

public record ConsumeJsonIncidence(Long userID, ScheduleEventEnum incidenceType,
                                   Boolean justified, LocalDateTime incidenceDate) {
}

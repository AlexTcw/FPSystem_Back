package com.fps.back.entry.model.dto.response;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

public record ResponseJsonIncidence(Long userID, Long ScheduleID, Long incidenceID, ScheduleEventEnum incidenceType,
                                    Boolean justified, OffsetDateTime createdAt, LocalDateTime incidenceDate) {
}

package com.fps.back.entry.model.dto.response;

import com.fps.back.entry.model.enums.IncidenceTypeEnum;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

public record ResponseJsonIncidence(Long userID, Long ScheduleID, Long incidenceID, IncidenceTypeEnum incidenceType,
                                    Boolean justified, OffsetDateTime createdAt, LocalDateTime incidenceDate) {
}

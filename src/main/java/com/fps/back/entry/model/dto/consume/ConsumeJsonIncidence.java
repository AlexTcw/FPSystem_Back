package com.fps.back.entry.model.dto.consume;

import com.fps.back.entry.model.enums.IncidenceTypeEnum;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

public record ConsumeJsonIncidence(Long userID, IncidenceTypeEnum incidenceType,
                                   Boolean justified, LocalDateTime incidenceDate) {
}

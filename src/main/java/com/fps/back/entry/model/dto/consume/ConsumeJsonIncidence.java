package com.fps.back.entry.model.dto.consume;

import com.fps.back.entry.model.enums.TypeRecordEnum;

import java.time.LocalDateTime;

public record ConsumeJsonIncidence(Long userID, TypeRecordEnum incidenceType,
                                   Boolean justified, LocalDateTime incidenceDate) {
}

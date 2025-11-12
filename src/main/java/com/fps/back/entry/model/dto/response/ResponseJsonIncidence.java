package com.fps.back.entry.model.dto.response;

import com.fps.back.entry.model.enums.TypeRecordEnum;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

public record ResponseJsonIncidence(Long userID, Long ScheduleID, Long incidenceID, TypeRecordEnum incidenceType,
                                    Boolean justified, OffsetDateTime createdAt) {
}

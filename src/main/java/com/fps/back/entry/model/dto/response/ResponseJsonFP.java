package com.fps.back.entry.model.dto.response;

import java.time.OffsetDateTime;
import java.util.Date;

public record ResponseJsonFP(Long userID, Long FPId,Integer deviceID, OffsetDateTime created_at, OffsetDateTime updated_at, boolean isActive, OffsetDateTime inactive_at ) {
}

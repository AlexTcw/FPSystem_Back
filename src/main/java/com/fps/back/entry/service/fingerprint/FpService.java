package com.fps.back.entry.service.fingerprint;

import com.fps.back.entry.model.dto.consume.ConsumeJsonFP;
import com.fps.back.entry.model.dto.consume.ConsumeJsonLong;
import com.fps.back.entry.model.dto.response.ResponseJsonFP;
import com.fps.back.entry.model.dto.response.ResponseJsonFPs;

public interface FpService {
    ResponseJsonFPs findFPByUserId(ConsumeJsonLong consume);

    ResponseJsonFP createOrUpdateFingerprint(ConsumeJsonFP consume);
}

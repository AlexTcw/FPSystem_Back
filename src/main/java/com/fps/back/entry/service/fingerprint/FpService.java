package com.fps.back.entry.service.fingerprint;

import com.fps.back.entry.model.dto.consume.ConsumeJsonFP;
import com.fps.back.entry.model.dto.consume.ConsumeJsonLong;
import com.fps.back.entry.model.dto.response.ResponseJsonFP;
import com.fps.back.entry.model.dto.response.ResponseJsonFPs;
import com.fps.back.entry.model.dto.response.ResponseJsonInteger;
import com.fps.back.entry.model.dto.response.ResponseJsonRecordEntry;
import org.springframework.transaction.annotation.Transactional;

public interface FpService {
    @Transactional(readOnly = true)
    ResponseJsonInteger getUsersCount();

    @Transactional(readOnly = true)
    ResponseJsonFP findActiveFPByDeviceId(ConsumeJsonLong consume);

    @Transactional(readOnly = true)
    ResponseJsonFP findFPByFBId(ConsumeJsonLong consume);

    ResponseJsonFPs findFPByUserId(ConsumeJsonLong consume);

    ResponseJsonFP createOrUpdateFingerprint(ConsumeJsonFP consume);

    ResponseJsonRecordEntry createRecord(ConsumeJsonLong consume);
}

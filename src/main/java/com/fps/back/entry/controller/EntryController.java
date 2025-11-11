package com.fps.back.entry.controller;

import com.fps.back.entry.model.dto.consume.ConsumeJsonFP;
import com.fps.back.entry.model.dto.consume.ConsumeJsonLong;
import com.fps.back.entry.model.dto.response.ResponseJsonFP;
import com.fps.back.entry.model.dto.response.ResponseJsonFPs;
import com.fps.back.entry.model.dto.response.ResponseJsonRecordEntry;
import com.fps.back.entry.service.fingerprint.FpService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class EntryController {

    private final FpService fpService;

    @MessageMapping("entry")
    @SendTo("/topic/entry")
    public String greeting(String message){
        return message;
    }

    @MessageMapping("createEntry")
    @SendTo("/topic/createdEntries")
    public ResponseJsonFP createEntry(ConsumeJsonFP consume){
        return fpService.createOrUpdateFingerprint(consume);
    }

    @MessageMapping("getEntryByUserID")
    @SendTo("/topic/entriesByUser")
    public ResponseJsonFPs getEntryByUserID(ConsumeJsonLong consume){
        return fpService.findFPByUserId(consume);
    }

    @MessageMapping("getEntryByFPID")
    @SendTo("/topic/entriesByFP_ID")
    public ResponseJsonFP getEntryByFPID(ConsumeJsonLong consume){
        return fpService.findFPByFBId(consume);
    }

    @MessageMapping("getEntryByDeviceID")
    @SendTo("/topic/entriesByDeviceId")
    public ResponseJsonFP getEntryByDeviceID(ConsumeJsonLong consume){
        return fpService.findActiveFPByDeviceId(consume);
    }

    @MessageMapping("createRecord")
    @SendTo("/topic/records")
    public ResponseJsonRecordEntry createRecord(ConsumeJsonLong consume){
        return fpService.createRecord(consume);
    }

}

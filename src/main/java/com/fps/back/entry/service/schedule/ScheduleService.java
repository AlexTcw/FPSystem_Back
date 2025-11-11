package com.fps.back.entry.service.schedule;

import com.fps.back.entry.model.dto.consume.ConsumeJsonSchedule;
import com.fps.back.entry.model.dto.response.ResponseJsonSchedule;

public interface ScheduleService {
    ResponseJsonSchedule createSchedule(ConsumeJsonSchedule consume);
}

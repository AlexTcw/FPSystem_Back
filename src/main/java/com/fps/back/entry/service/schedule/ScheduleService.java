package com.fps.back.entry.service.schedule;

import com.fps.back.entry.model.dto.consume.ConsumeJsonSchedule;
import com.fps.back.entry.model.dto.dto.UserSchedule;
import com.fps.back.entry.model.dto.response.ResponseJsonSchedule;
import com.fps.back.entry.model.dto.response.ResponseJsonUserScheduleDetail;
import org.springframework.data.domain.Page;

public interface ScheduleService {
    ResponseJsonSchedule createSchedule(ConsumeJsonSchedule consume);
    ResponseJsonSchedule updateSchedule(Long userID);
    Page<UserSchedule> getUserSchedules(Integer page, Integer size, String keyword);

    ResponseJsonUserScheduleDetail getScheduleDetail(Long scheduleID);
}

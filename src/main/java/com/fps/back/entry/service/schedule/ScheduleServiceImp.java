package com.fps.back.entry.service.schedule;

import com.fps.back.entry.model.dto.consume.ConsumeJsonSchedule;
import com.fps.back.entry.model.dto.response.ResponseJsonSchedule;
import com.fps.back.entry.model.entity.Schedule;
import com.fps.back.entry.model.entity.ScheduleDetail;
import com.fps.back.entry.model.entity.User;
import com.fps.back.entry.model.enums.ScheduleTypeEnum;
import com.fps.back.entry.repository.ScheduleRepository;
import com.fps.back.entry.repository.UserEntryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ScheduleServiceImp implements  ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final UserEntryRepository userEntryRepository;

    @Override
    @Transactional
    public ResponseJsonSchedule createSchedule(ConsumeJsonSchedule consume){

        if (userEntryRepository.existsUserByUserId(consume.userID())){
            throw new IllegalArgumentException("user with id: "+consume.userID()+" not found");
        }

        User currentUser = userEntryRepository.findUserByUserId(consume.userID());
        if (scheduleRepository.existActiveScheduleForUser(consume.userID())){
                throw new IllegalArgumentException("user with id: "+consume.userID()+" is already active.");
        }
        System.out.println(consume.schedule_type());
        Schedule schedule = Schedule.builder()
                .scheduleType(ScheduleTypeEnum.valueOf(consume.schedule_type().toUpperCase()))
                .startDate(consume.startDate())
                .details(consume.daysOfWeek().stream().map(day ->
                        ScheduleDetail.builder().dayOfWeek(day.day_of_week()).entryTime(day.entry_time())
                                .exitTime(day.exit_time()).build()).toList())
                .build();

        currentUser.getSchedules().add(schedule);

        userEntryRepository.save(currentUser);
        return new ResponseJsonSchedule(currentUser.getUserId(),schedule.getStartDate().toString(),schedule.getScheduleType().name());
    }


}

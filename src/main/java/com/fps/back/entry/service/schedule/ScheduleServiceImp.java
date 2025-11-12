package com.fps.back.entry.service.schedule;

import com.fps.back.entry.model.dto.consume.ConsumeJsonSchedule;
import com.fps.back.entry.model.dto.dto.DayScheduleDTO;
import com.fps.back.entry.model.dto.dto.UserSchedule;
import com.fps.back.entry.model.dto.response.ResponseJsonSchedule;
import com.fps.back.entry.model.dto.response.ResponseJsonUserScheduleDetail;
import com.fps.back.entry.model.entity.Schedule;
import com.fps.back.entry.model.entity.ScheduleDetail;
import com.fps.back.entry.model.entity.User;
import com.fps.back.entry.repository.ScheduleRepository;
import com.fps.back.entry.repository.UserEntryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ScheduleServiceImp implements  ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final UserEntryRepository userEntryRepository;

    @Override
    @Transactional
    public ResponseJsonSchedule createSchedule(@RequestBody ConsumeJsonSchedule consume){

        if (consume == null){
            throw new IllegalArgumentException("Consume is null");
        }

        User currentUser = getValidUserByUserId(consume.userID());

        if (scheduleRepository.existActiveScheduleForUser(consume.userID())){
                throw new IllegalArgumentException("user with id: "+consume.userID()+" is already active.");
        }

        Schedule schedule = Schedule.builder()
                .scheduleType(consume.schedule_type())
                .startDate(consume.startDate())
                .isActive(true)
                .createdAt(LocalDateTime.now())
                .build();

        Schedule finalSchedule = schedule;
        List<ScheduleDetail> details = consume.daysOfWeek().stream()
                .map(day -> ScheduleDetail.builder()
                        .dayOfWeek(day.day_of_week())
                        .entryTime(day.entry_time())
                        .exitTime(day.exit_time())
                        .schedule(finalSchedule)
                        .build())
                .toList();

        schedule.setDetails(details);

        schedule = scheduleRepository.save(schedule);

        currentUser.getSchedules().add(schedule);
        userEntryRepository.save(currentUser);

        return new ResponseJsonSchedule(currentUser.getUserId(),schedule.getStartDate().toString(),schedule.getScheduleType().name());
    }

    @Override
    public ResponseJsonSchedule updateSchedule(Long userID) {

        if (!scheduleRepository.existActiveScheduleForUser(userID)){
            throw new IllegalArgumentException("user with id: "+userID+" hasn't an active schedule.");
        }

        User currentUser = getValidUserByUserId(userID);
        Schedule currentSchedule = scheduleRepository.findActiveScheduleForUser(currentUser.getUserId());
        currentSchedule.setEndDate(LocalDateTime.now());
        currentSchedule.setIsActive(false);
        scheduleRepository.save(currentSchedule);

        return new ResponseJsonSchedule(currentUser.getUserId(),currentSchedule.getStartDate().toString(),currentSchedule.getScheduleType().name());
    }

    @Override
    public Page<UserSchedule> getUserSchedules(Integer page, Integer size, String keyword) {
        Pageable pageable = PageRequest.of(page, size);
        return scheduleRepository.findAllSchedule(pageable,keyword);
    }

    private User getValidUserByUserId(Long userID){

        if (!userEntryRepository.existsUserByUserId(userID)){
            throw new IllegalArgumentException("user with id: "+userID+" not found");
        }

        return  userEntryRepository.findUserByUserId(userID);
    }

    @Override
    public ResponseJsonUserScheduleDetail getScheduleDetail(Long scheduleID){
        if (scheduleID <= 0){
            throw new IllegalArgumentException("invalid ID");
        }
        if (!scheduleRepository.existsScheduleByScheduleId((scheduleID))){
            throw new IllegalArgumentException("schedule with id: "+scheduleID+" not found");
        }
        ResponseJsonUserScheduleDetail response = scheduleRepository.findUserScheduleDetailByScheduleId(scheduleID);
        Set<DayScheduleDTO> daysOfWeek = scheduleRepository.findDetailsByScheduleId(scheduleID);
        return new ResponseJsonUserScheduleDetail(
                response.userId(),
                response.name(),
                response.email(),
                response.username(),
                response.scheduleId(),
                response.createdAt(),
                response.endDate(),
                response.isActive(),
                response.scheduleType(),
                daysOfWeek,
                response.status()
        );
    }




}

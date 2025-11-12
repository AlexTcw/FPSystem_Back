package com.fps.back.entry.service.attendance;

import com.fps.back.entry.model.dto.response.ResponseJsonIncidence;
import com.fps.back.entry.repository.ScheduleEventRepository;
import com.fps.back.entry.repository.ScheduleRepository;
import com.fps.back.entry.repository.UserEntryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AttendanceServiceImp implements AttendanceService {
    private final ScheduleRepository scheduleRepository;
    private final UserEntryRepository userRepository;
    private final ScheduleEventRepository scheduleEventRepository;


    @Override
    @Transactional(readOnly = true)
    public Set<ResponseJsonIncidence> getIncidenceByScheduleIdAndDate(Long scheduleID, LocalDateTime startDate, LocalDateTime endDate) {

        if (scheduleID <= 0){
            throw new IllegalArgumentException("Invalid Id");
        }

        if(!scheduleRepository.existsScheduleByScheduleId(scheduleID)){
            throw new IllegalArgumentException("Schedule with id " + scheduleID + " does not exist");
        }

        if (startDate == null) {
            startDate = LocalDateTime.now().minusMonths(1);
        }

        if (endDate == null) {
            endDate = LocalDateTime.now();
        }

        return scheduleEventRepository.getIncidenceByScheduleIdAndDate(scheduleID, startDate, endDate);
    }

}

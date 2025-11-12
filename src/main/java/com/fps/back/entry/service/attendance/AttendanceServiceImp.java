package com.fps.back.entry.service.attendance;

import com.fps.back.entry.repository.ScheduleRepository;
import com.fps.back.entry.repository.UserEntryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AttendanceServiceImp implements AttendanceService {
    private final ScheduleRepository scheduleRepository;
    private final UserEntryRepository userRepository;



}

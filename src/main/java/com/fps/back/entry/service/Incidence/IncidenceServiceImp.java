package com.fps.back.entry.service.Incidence;

import com.fps.back.entry.repository.ScheduleRepository;
import com.fps.back.entry.repository.UserEntryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class IncidenceServiceImp implements IncidenceService {
    private final ScheduleRepository scheduleRepository;
    private final UserEntryRepository userRepository;



}

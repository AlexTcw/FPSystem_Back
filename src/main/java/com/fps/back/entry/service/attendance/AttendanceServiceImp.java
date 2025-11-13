package com.fps.back.entry.service.attendance;

import com.fps.back.entry.model.dto.response.ResponseJsonAttendance;
import com.fps.back.entry.model.dto.response.ResponseJsonIncidence;
import com.fps.back.entry.model.entity.Schedule;
import com.fps.back.entry.model.entity.ScheduleEvent;
import com.fps.back.entry.model.entity.User;
import com.fps.back.entry.model.enums.TypeRecordEnum;
import com.fps.back.entry.repository.ScheduleEventRepository;
import com.fps.back.entry.repository.ScheduleRepository;
import com.fps.back.entry.repository.UserEntryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.*;
import java.util.HashSet;
import java.util.List;
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

    @Override
    @Transactional(readOnly = true)
    public Page<ResponseJsonAttendance> getAttendancePage(LocalDateTime startDate, LocalDateTime endDate,
                                                          String keyword, int page, int pageSize) {

        if (startDate == null) {
            startDate = LocalDateTime.now().minusMonths(1);
        }

        if (endDate == null) {
            endDate = LocalDateTime.now();
        }

        Pageable pageable = PageRequest.of(page, pageSize);

        return scheduleEventRepository.getAttendancePage(keyword, startDate, endDate, pageable);
    }

    @Override
    @Transactional
    public void createAttendance() {
        LocalDate today = LocalDate.now();

        // Obtener todos los usuarios
        Set<User> users = new HashSet<>(userRepository.findAll());

        users.forEach(user -> {
            // Iterar sobre los horarios activos
            user.getSchedules().stream()
                    .filter(Schedule::getIsActive)
                    .forEach(schedule -> {

                        // Filtrar eventos del día actual
                        List<ScheduleEvent> todayEvents = schedule.getEvents().stream()
                                .filter(e -> e.getCreatedAt().toLocalDate().isEqual(today))
                                .toList();

                        // Si no tiene eventos -> ausencia
                        if (todayEvents.isEmpty()) {
                            ScheduleEvent absence = new ScheduleEvent();
                            absence.setCreatedAt(OffsetDateTime.now());
                            absence.setIncidenceType(TypeRecordEnum.ABSENCE);
                            absence.setJustified(false);
                            absence.setSchedule(schedule);
                            scheduleEventRepository.save(absence);
                            return;
                        }

                        // Buscar entrada y salida del día
                        ScheduleEvent entry = todayEvents.stream()
                                .filter(e -> e.getEventType() == TypeRecordEnum.ENTRY)
                                .findFirst().orElse(null);

                        ScheduleEvent exit = todayEvents.stream()
                                .filter(e -> e.getEventType() == TypeRecordEnum.EXIT)
                                .findFirst().orElse(null);

                        // Validar retardo (ejemplo: más de 30 min después del horario de inicio)
                        if (entry != null && schedule.getStartDate() != null) {
                            LocalTime scheduledStart = schedule.getStartDate().toLocalTime();
                            LocalTime actualEntry = entry.getCreatedAt().toLocalTime();

                            if (Duration.between(scheduledStart, actualEntry).toMinutes() > 30) {
                                ScheduleEvent delay = new ScheduleEvent();
                                delay.setCreatedAt(OffsetDateTime.now());
                                delay.setIncidenceType(TypeRecordEnum.DELAY);
                                delay.setJustified(false);
                                delay.setSchedule(schedule);
                                scheduleEventRepository.save(delay);
                            }
                        }

                        // Si no tiene salida registrada
                        if (exit == null) {
                            ScheduleEvent missingExit = new ScheduleEvent();
                            missingExit.setCreatedAt(OffsetDateTime.now());
                            missingExit.setIncidenceType(TypeRecordEnum.UNKNOWN);
                            missingExit.setJustified(false);
                            missingExit.setSchedule(schedule);
                            scheduleEventRepository.save(missingExit);
                        }

                        scheduleRepository.save(schedule);
                    });
        });
    }


}

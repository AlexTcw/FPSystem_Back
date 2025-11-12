package com.fps.back.entry.controller;

import com.fps.back.entry.model.dto.consume.ConsumeJsonSchedule;
import com.fps.back.entry.model.dto.dto.UserSchedule;
import com.fps.back.entry.model.dto.response.ResponseJsonSchedule;
import com.fps.back.entry.model.dto.response.ResponseJsonUser;
import com.fps.back.entry.model.dto.response.ResponseJsonUserScheduleDetail;
import com.fps.back.entry.service.schedule.ScheduleService;
import com.fps.back.entry.service.userEntry.UserEntryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;


@RestController
@RequestMapping("entry")
@RequiredArgsConstructor
public class UsersEntryController  {

    private final UserEntryService userEntryService;
    private final ScheduleService scheduleService;


    @GetMapping(value = "users/{page}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<ResponseJsonUser>> getUserPage(
            @PathVariable(name = "page", required = false) Integer page,
            @RequestParam(name = "keyword", required = false, defaultValue = "") String keyword,
            @RequestParam(name = "size", required = false, defaultValue = "10") Integer size) {
        return new ResponseEntity<>(
                userEntryService.findUsersByKeyword(keyword, page <= 1 ? 0 : page, size),
                HttpStatus.OK);
    }

    @PostMapping(value = "schedule", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseJsonSchedule>  createSchedule(@RequestBody ConsumeJsonSchedule consume){
        return new ResponseEntity<>(scheduleService.createSchedule(consume), HttpStatus.OK);
    }

    @PutMapping(value = "schedule/{id}",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseJsonSchedule> updateSchedule(@PathVariable Long id){
        return new ResponseEntity<>(scheduleService.updateSchedule(id), HttpStatus.OK);
    }

    @GetMapping(value = "schedule/{page}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<UserSchedule>> getSchedulePage(
            @PathVariable(name = "page", required = false) Integer page,
            @RequestParam(name = "keyword", required = false, defaultValue = "") String keyword,
            @RequestParam(name = "size", required = false, defaultValue = "10") Integer size) {
        return new ResponseEntity<>(
                scheduleService.getUserSchedules(page <= 1 ? 0 : page, size,keyword),
                HttpStatus.OK);
    }

    @GetMapping(value = "schedule/details/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseJsonUserScheduleDetail> getUserScheduleDetail(@PathVariable Long id){
        return new ResponseEntity<>(scheduleService.getScheduleDetail(id), HttpStatus.OK);
    }
}

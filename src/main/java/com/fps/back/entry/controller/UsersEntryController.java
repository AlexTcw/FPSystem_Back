package com.fps.back.entry.controller;

import com.fps.back.entry.model.dto.response.ResponseJsonUser;
import com.fps.back.entry.service.user_entry.UserEntryService;
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


    @GetMapping(value = "users/{page}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<ResponseJsonUser>> getUserPage(
            @PathVariable(name = "page", required = false) Integer page,
            @RequestParam(name = "keyword", required = false, defaultValue = "") String keyword,
            @RequestParam(name = "size", required = false, defaultValue = "10") Integer size) {
        return new ResponseEntity<>(
                userEntryService.findUsersByKeyword(keyword, page <= 1 ? 0 : page, size),
                HttpStatus.OK);
    }
}

package com.fps.back.users.controller;

import com.fps.back.users.model.dto.Consume.ConsumeJsonLogin;
import com.fps.back.users.model.dto.Consume.ConsumeJsonString;
import com.fps.back.users.model.dto.Response.ResponseJsonLogin;
import com.fps.back.users.model.dto.Response.ResponseJsonString;
import com.fps.back.users.service.users.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("auth")
public class Auth {
    private final UserService userService;

    @PostMapping(value = {"/login"}, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseJsonLogin> login(@RequestBody ConsumeJsonLogin consume) {
        return ResponseEntity.ok(userService.login(consume));
    }

    @PostMapping(value = {"/bcrypt"}, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseJsonString> bcrypt(@RequestBody ConsumeJsonString consume) {
        return ResponseEntity.ok(userService.bcrypt(consume));
    }
}

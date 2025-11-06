package com.fps.back.users.controller;

import com.fps.back.users.model.dto.Consume.ConsumeJsonUser;
import com.fps.back.users.model.dto.Response.ResponseJsonNumberGeneric;
import com.fps.back.users.model.dto.Response.ResponseJsonUser;
import com.fps.back.users.model.dto.Response.ResponseJsonUsers;
import com.fps.back.users.service.users.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("user")
public class UserController {

    private final UserService userService;

    @GetMapping(value = "role", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseJsonNumberGeneric> getRoles() {
        return new ResponseEntity<>(userService.getRoles(), HttpStatus.OK);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseJsonUser> createUser(@RequestBody ConsumeJsonUser consume) {
        return new ResponseEntity<>(userService.createOrUpdateUser(consume,null), HttpStatus.OK);
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseJsonUser> updateUser( @RequestParam("id") Long userID,@RequestBody ConsumeJsonUser consume) {
        return new ResponseEntity<>(userService.createOrUpdateUser(consume,userID), HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity<Object> findUserById(@PathVariable Long id) {
        return new ResponseEntity<>(userService.findUserByUserId(id), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<ResponseJsonUsers> findAllUsers() {
        return new ResponseEntity<>(userService.findAllUsers(), HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteUserById(@PathVariable Long id) {
        userService.deleteUserById(id);
        return new ResponseEntity<>("User with id: "+id +" deleted successfully",HttpStatus.OK);
    }
}

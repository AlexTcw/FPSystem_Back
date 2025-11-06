package com.fps.back.users.service.users;

import com.fps.back.users.model.dto.Consume.ConsumeJsonLogin;
import com.fps.back.users.model.dto.Consume.ConsumeJsonString;
import com.fps.back.users.model.dto.Consume.ConsumeJsonUser;
import com.fps.back.users.model.dto.Response.*;

public interface UserService {
    ResponseJsonString bcrypt(ConsumeJsonString consume);

    ResponseJsonLogin login(ConsumeJsonLogin consume);

    ResponseJsonNumberGeneric getRoles();

    ResponseJsonUser findUserByUserId(Long userID);

    ResponseJsonUsers findAllUsers();

    ResponseJsonUser createOrUpdateUser(ConsumeJsonUser consume, Long userId);

    void deleteUserById(Long id);
}

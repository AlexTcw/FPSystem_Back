package com.fps.back.users.service.users;

import com.fps.back.users.model.dto.Consume.ConsumeJsonLogin;
import com.fps.back.users.model.dto.Consume.ConsumeJsonString;
import com.fps.back.users.model.dto.Response.ResponseJsonLogin;
import com.fps.back.users.model.dto.Response.ResponseJsonString;

public interface UserService {
    ResponseJsonString bcrypt(ConsumeJsonString consume);

    ResponseJsonLogin login(ConsumeJsonLogin consume);
}

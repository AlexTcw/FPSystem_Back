package com.fps.back.users.model.dto.Consume;

import java.util.List;

public record ConsumeJsonUser(String firstName, String lastName,
                              String phone, String address,
                              String email, String username,
                              String password, Long rolID) {
}

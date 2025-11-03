package com.fps.back.users.model.dto.Response;

import java.util.Set;

public record ResponseJsonLogin(long userId, Set<String> permissions, Set<String> roles, String username, String jwtToken) {
}

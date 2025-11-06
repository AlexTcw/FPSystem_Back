package com.fps.back.users.model.dto.Response;

import java.util.Set;

public record ResponseJsonUser(Long userId, Set<String> permissions, Set<String> roles, String username, String email, String name) {
}

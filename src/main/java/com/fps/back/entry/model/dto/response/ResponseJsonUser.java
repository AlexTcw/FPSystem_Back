package com.fps.back.entry.model.dto.response;

public record ResponseJsonUser(String name, String username, String email, String phone, String address,
                               Long FingerprintID, Boolean isActive, String created_at, String updated_at, String inactive_at) {
}

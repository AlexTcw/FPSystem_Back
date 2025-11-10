package com.fps.back.entry.service.user_entry;

import com.fps.back.entry.model.dto.response.ResponseJsonUser;
import org.springframework.data.domain.Page;

public interface UserEntryService {
    Page<ResponseJsonUser> findUsersByKeyword(String keyword, int page, int size);
}

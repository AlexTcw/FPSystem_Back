package com.fps.back.entry.service.userEntry;

import com.fps.back.entry.model.dto.response.ResponseJsonUser;
import org.springframework.data.domain.Page;

public interface UserEntryService {
    Page<ResponseJsonUser> findUsersByKeyword(String keyword, int page, int size);
}

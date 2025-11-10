package com.fps.back.entry.service.user_entry;

import com.fps.back.entry.model.dto.response.ResponseJsonUser;
import com.fps.back.entry.repository.UserEntryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserEntryServiceImp implements UserEntryService {

    private final UserEntryRepository userEntryRepository;

    @Override
    public Page<ResponseJsonUser> findUsersByKeyword(String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return userEntryRepository.findUsersByKeyword(keyword, pageable);
    }
}

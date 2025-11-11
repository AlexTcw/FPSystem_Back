package com.fps.back.entry.repository;

import com.fps.back.entry.model.entity.Records;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RecordRepository extends JpaRepository<Records,Long> {

}

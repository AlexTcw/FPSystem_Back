package com.fps.back.entry.repository;

import com.fps.back.entry.model.entity.Fingerprint;
import com.fps.back.entry.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FpRepository extends JpaRepository<Fingerprint, Long> {
    boolean existsFingerprintByUsuario_UserId(Long usuarioUserId);

    Fingerprint findFingerprintByFingerprintId(Long fingerprintId);

    List<Fingerprint> findFingerprintByUsuario(User usuario);
}

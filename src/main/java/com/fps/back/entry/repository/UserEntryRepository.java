package com.fps.back.entry.repository;

import com.fps.back.entry.model.dto.response.ResponseJsonUser;
import com.fps.back.entry.model.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Page;

public interface UserEntryRepository extends JpaRepository<User, Long> {
    User findUserByUserId(Long userId);

    boolean existsUserByUserId(Long userId);

    @Query(value = """
        SELECT new com.fps.back.entry.model.dto.response.ResponseJsonUser(
            U.firstName || ' ' || U.lastName,
            U.username,
            U.email,
            U.phone,
            U.address,
            F.fingerprintId,
            F.isActive,
            CAST(F.createdAt AS string),
            CAST(F.updatedAt AS string),
            CAST(F.inactiveAt AS string)
        )
        FROM User_entry U
        JOIN U.fingerprints F
        WHERE :keyword IS NULL
            OR :keyword = ''
            OR CAST(U.userId AS string) = :keyword
            OR UPPER(translate(U.username, 'ñáéíóúàèìòùãõâêîôûäëïöüç', 'naeiouaeiouaeiouc'))
               LIKE CONCAT('%', UPPER(translate(REPLACE(:keyword, '_', ''), 'ñáéíóúàèìòùãõâêîôûäëïöüç', 'naeiouaeiouaeiouc')), '%')
            OR UPPER(translate(U.email, 'ñáéíóúàèìòùãõâêîôûäëïöüç', 'naeiouaeiouaeiouc'))
               LIKE CONCAT('%', UPPER(translate(REPLACE(:keyword, '_', ''), 'ñáéíóúàèìòùãõâêîôûäëïöüç', 'naeiouaeiouaeiouc')), '%')
        ORDER BY F.createdAt DESC
        """)
    Page<ResponseJsonUser> findUsersByKeyword(@Param("keyword") String keyword, Pageable pageable);
}


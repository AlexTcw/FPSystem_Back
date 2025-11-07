package com.fps.back.entry.service.fingerprint;

import com.fps.back.entry.model.dto.consume.ConsumeJsonFP;
import com.fps.back.entry.model.dto.consume.ConsumeJsonLong;
import com.fps.back.entry.model.dto.response.ResponseJsonFP;
import com.fps.back.entry.model.dto.response.ResponseJsonFPs;
import com.fps.back.entry.model.entity.Fingerprint;
import com.fps.back.entry.model.entity.User;
import com.fps.back.entry.repository.FpRepository;
import com.fps.back.entry.repository.UserEntryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class FpServiceImp implements FpService{

    private final FpRepository fpRepository;
    private final UserEntryRepository userEntryRepository;

    @Override
    @Transactional(readOnly = true)
    public ResponseJsonFPs findFPByUserId(ConsumeJsonLong consume) {
        if (consume == null) {
            throw new IllegalArgumentException("Consume is null");
        }
        if (consume.key() == null) {
            throw new IllegalArgumentException("Consume key is null");
        }

        if (!userEntryRepository.existsById(consume.key())) {
            throw new IllegalArgumentException("User does not exist with id " + consume.key());
        }

        User user = userEntryRepository.findUserByUserId(consume.key());
        Set<Fingerprint> userFingerprints = new HashSet<>(fpRepository.findFingerprintByUsuario(user));

        Set<ResponseJsonFP> responseJsonFPs = new HashSet<>();
        userFingerprints.forEach(fingerprint -> {
           responseJsonFPs.add(new ResponseJsonFP(user.getUserId(),fingerprint.getDeviceId(),fingerprint.getCreatedAt(),fingerprint.getUpdatedAt(),fingerprint.getIsActive(),fingerprint.getInactiveAt()));
        });
        return new ResponseJsonFPs(responseJsonFPs);

    }

    @Override
    public ResponseJsonFP createOrUpdateFingerprint(ConsumeJsonFP consume){
        if (consume == null) {
            throw new IllegalArgumentException("Consume cannot be null");
        }

        if (consume.userID() == null) {
            throw new IllegalArgumentException("Consume userID cannot be null");
        }

        if(!userEntryRepository.existsUserByUserId(consume.userID())){
            throw new IllegalArgumentException("User does not exist with id " + consume.userID());
        }

        User user = userEntryRepository.findUserByUserId(consume.userID());
        Fingerprint fp;
        if(consume.fpId() == null){
            /*create*/
            fp =  createFP(user,consume);
        } else {
            fp = fpRepository.findFingerprintByFingerprintId(consume.fpId());
            fp = editFP(fp,user,consume);
        }
        /*edit*/
        fp = fpRepository.save(fp);

        return new ResponseJsonFP(user.getUserId(),fp.getDeviceId(),
                fp.getCreatedAt(),fp.getUpdatedAt(),fp.getIsActive(), null);
    }

    Fingerprint createFP(User user, ConsumeJsonFP consume){
        return Fingerprint.builder()
                .usuario(user)
                .deviceId(consume.deviceID())
                .createdAt(OffsetDateTime.now())
                .updatedAt(OffsetDateTime.now())
                .isActive(true)
                .build();
    }

    Fingerprint editFP(Fingerprint fp, User user, ConsumeJsonFP consume){
        OffsetDateTime inactiveAT = !consume.isActive() ? OffsetDateTime.now() : null;
        return fp.toBuilder()
                .usuario(user)
                .isActive(consume.isActive())
                .deviceId(consume.deviceID())
                .inactiveAt(inactiveAT)
                .updatedAt(OffsetDateTime.now())
                .build();
    }


}

package com.fps.back.entry.service.fingerprint;

import com.fps.back.entry.model.dto.consume.ConsumeJsonFP;
import com.fps.back.entry.model.dto.consume.ConsumeJsonLong;
import com.fps.back.entry.model.dto.response.ResponseJsonFP;
import com.fps.back.entry.model.dto.response.ResponseJsonFPs;
import com.fps.back.entry.model.dto.response.ResponseJsonInteger;
import com.fps.back.entry.model.dto.response.ResponseJsonRecordEntry;
import com.fps.back.entry.model.entity.Fingerprint;
import com.fps.back.entry.model.entity.Records;
import com.fps.back.entry.model.entity.User;
import com.fps.back.entry.model.enums.TypeRecordEnum;
import com.fps.back.entry.repository.FpRepository;
import com.fps.back.entry.repository.RecordRepository;
import com.fps.back.entry.repository.UserEntryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FpServiceImp implements FpService{

    private final FpRepository fpRepository;
    private final UserEntryRepository userEntryRepository;
    private final RecordRepository recordRepository;


    @Override
    @Transactional(readOnly = true)
    public ResponseJsonInteger getUsersCount(){
        return new ResponseJsonInteger((int)userEntryRepository.count());
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseJsonFP findActiveFPByDeviceId(ConsumeJsonLong consume) {
        validConsumeJsonLong(consume);

        if ((!fpRepository.existsFingerprintByDeviceIdAndIsActive(consume.key().intValue(), true))) {
            throw new IllegalArgumentException("Fingerprint with device id: "+consume.key()+" does not exist or is inactive.");
        }

        Fingerprint fp = fpRepository.findFingerprintByDeviceIdAndIsActive(consume.key().intValue(), true);
        User user = fp.getUsuario();
        return new ResponseJsonFP(
                user.getUserId(),
                fp.getFingerprintId(),
                fp.getDeviceId(),
                fp.getCreatedAt(),
                fp.getUpdatedAt(),
                fp.getIsActive(),
                fp.getInactiveAt());
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseJsonFP findFPByFBId(ConsumeJsonLong consume) {
        validConsumeJsonLong(consume);

        if (!fpRepository.existsFingerprintByFingerprintId(consume.key())) {
            throw new IllegalArgumentException("Fingerprint with id: "+consume.key()+" does not exist");
        }

        Fingerprint fp = fpRepository.findFingerprintByFingerprintId(consume.key());
        User user = fp.getUsuario();
        return new ResponseJsonFP(
                user.getUserId(),
                fp.getFingerprintId(),
                fp.getDeviceId(),
                fp.getCreatedAt(),
                fp.getUpdatedAt(),
                fp.getIsActive(),
                fp.getInactiveAt());
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseJsonFPs findFPByUserId(ConsumeJsonLong consume) {
        validConsumeJsonLong(consume);

        if (!userEntryRepository.existsById(consume.key())) {
            throw new IllegalArgumentException("User does not exist with id " + consume.key());
        }

        User user = userEntryRepository.findUserByUserId(consume.key());

        return new ResponseJsonFPs(getFPResponse(user));

    }


    @Override
    @Transactional
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

        return new ResponseJsonFP(user.getUserId(),fp.getFingerprintId(),fp.getDeviceId(),
                fp.getCreatedAt(),fp.getUpdatedAt(),fp.getIsActive(), null);
    }

    @Override
    public ResponseJsonRecordEntry createRecord(ConsumeJsonLong consume) {
        ResponseJsonRecordEntry response;

            // 1. Verificar huella activa
            Fingerprint fp = fpRepository.findFingerprintByDeviceIdAndIsActive(
                    consume.key().intValue(), true);

            if (fp == null) {
                getRecord();
                throw new IllegalArgumentException("Fingerprint with device id: " + consume.key() + " does not exist or is inactive.");
            }

            User user = fp.getUsuario();
            Set<Records> records = Optional.ofNullable(fp.getRecords()).orElseGet(HashSet::new);

            LocalDateTime now = LocalDateTime.now();
            String entryDate = null;
            String exitDate = null;

            // 2. Obtener último registro (si existe)
            Records lastRecord = records.stream()
                    .sorted(Comparator.comparing(Records::getTimestamp))
                    .reduce((first, second) -> second)
                    .orElse(null);

            // 3. Crear nuevo registro según el caso
            Records newRecord = new Records();

            if (lastRecord == null) {
                // No hay registros previos → Entrada
                newRecord.setType(TypeRecordEnum.ENTRY);
                entryDate = now.toString();
            } else if (lastRecord.getType() == TypeRecordEnum.ENTRY) {
                // Último fue entrada → ahora salida
                newRecord.setType(TypeRecordEnum.EXIT);
                entryDate = lastRecord.getTimestamp().toString();
                exitDate = now.toString();
            } else if (lastRecord.getType() == TypeRecordEnum.EXIT) {
                // Último fue salida → ahora entrada
                newRecord.setType(TypeRecordEnum.ENTRY);
                entryDate = now.toString();
            }

            // 4. Asignar hora y agregar al set
            newRecord.setTimestamp(now);

            /*peristimos para evitar un error de fluenttransacction*/
            newRecord = recordRepository.save(newRecord);
            records.add(newRecord);
            fp.setRecords(records);

            // 5. Persistir
            fpRepository.save(fp);

            // 6. Respuesta
            response = new ResponseJsonRecordEntry(user.getUserId(), entryDate, exitDate);

        return response;
    }


    private void getRecord() {
        Records record = new Records();
        record.setType(TypeRecordEnum.FAILED);
        record.setTimestamp(LocalDateTime.now());
        recordRepository.save(record);
    }


    private Set<ResponseJsonFP> getFPResponse(User user) {
        List<Fingerprint> fingerprints = fpRepository.findFingerprintByUsuario(user);

        return fingerprints.stream()
                .map(f -> new ResponseJsonFP(
                        user.getUserId(),
                        f.getFingerprintId(),
                        f.getDeviceId(),
                        f.getCreatedAt(),
                        f.getUpdatedAt(),
                        f.getIsActive(),
                        f.getInactiveAt()))
                .collect(Collectors.toSet());
    }

    private Fingerprint createFP(User user, ConsumeJsonFP consume){
        return Fingerprint.builder()
                .usuario(user)
                .deviceId(consume.deviceID())
                .createdAt(OffsetDateTime.now())
                .updatedAt(OffsetDateTime.now())
                .isActive(true)
                .build();
    }

    private Fingerprint editFP(Fingerprint fp, User user, ConsumeJsonFP consume){
        OffsetDateTime inactiveAT = !consume.isActive() ? OffsetDateTime.now() : null;
        return fp.toBuilder()
                .usuario(user)
                .isActive(consume.isActive())
                .deviceId(consume.deviceID())
                .inactiveAt(inactiveAT)
                .updatedAt(OffsetDateTime.now())
                .build();
    }

    private void validConsumeJsonLong(ConsumeJsonLong consume) {
        if (consume == null) {
            throw new IllegalArgumentException("Consume is null");
        }
        if (consume.key() == null) {
            throw new IllegalArgumentException("Consume key is null");
        }
    }


}

package com.fps.back.entry.model.entity;

import com.fps.back.entry.model.enums.TypeRecordEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(schema = "fps", name = "records")
public class Records implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "record_id")
    private Long recordId;
    @Enumerated(EnumType.STRING)
    @Column(name = "type_record", nullable = false)
    private TypeRecordEnum type;
}

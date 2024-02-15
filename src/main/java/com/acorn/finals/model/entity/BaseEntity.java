package com.acorn.finals.model.entity;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
public class BaseEntity {
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    public LocalDateTime createdAt;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    public LocalDateTime updatedAt;

    public void initDate(LocalDateTime creationTime) {
        createdAt = creationTime;
        updatedAt = creationTime;
    }

    public void initDate() {
        initDate(LocalDateTime.now());
    }

    public void updateDate(LocalDateTime updateTime) {
        updateTime = updateTime;
    }

    public void updateDate() {
        updateDate(LocalDateTime.now());
    }
}

package com.acorn.finals.model.entity;

import com.fasterxml.jackson.databind.ser.Serializers;
import lombok.*;
import org.apache.ibatis.type.Alias;

import java.time.LocalDateTime;
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Alias("messageEntity")
public class MessageEntity extends BaseEntity {
    Integer id;
    Integer authorId;
    String content;
    Integer channelId;
    Integer topicId;
}

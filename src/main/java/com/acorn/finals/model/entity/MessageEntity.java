package com.acorn.finals.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.ibatis.type.Alias;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Alias("messageEntity")
public class MessageEntity {
    Integer id;
    Integer authorId;
    String content;
    LocalDateTime sendDate;
    Integer channelId;
    Integer topicId;
}

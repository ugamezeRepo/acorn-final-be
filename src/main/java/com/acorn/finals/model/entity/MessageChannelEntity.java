package com.acorn.finals.model.entity;

import lombok.*;
import org.apache.ibatis.type.Alias;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Alias("messageChannelEntity")
public class MessageChannelEntity extends BaseEntity {
    Integer id;
    Integer authorId;
    String content;
    Integer channelId;
    Integer topicId;
}

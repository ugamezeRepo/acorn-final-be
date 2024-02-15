package com.acorn.finals.model.entity;

import lombok.*;
import org.apache.ibatis.type.Alias;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Alias("topicEntity")
public class TopicEntity extends BaseEntity {
    Integer id;
    String title;
    Integer channelId;
}

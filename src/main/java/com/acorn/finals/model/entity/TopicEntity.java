package com.acorn.finals.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.ibatis.type.Alias;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Alias("topicEntity")
public class TopicEntity {
    Integer id;
    String title;
    Integer channelId;
}

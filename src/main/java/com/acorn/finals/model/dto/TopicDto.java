package com.acorn.finals.model.dto;

import com.acorn.finals.model.entity.TopicEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TopicDto {
    String title;

    public TopicEntity toEntity(Integer channelId, Integer topicId) {
        return TopicEntity.builder()
                .id(topicId)
                .title(title)
                .channelId(channelId)
                .build();
    }
}

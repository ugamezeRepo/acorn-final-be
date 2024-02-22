package com.acorn.finals.model.dto;

import com.acorn.finals.model.entity.TopicEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TopicDto {
    int id;
    String title;

    public TopicEntity toEntity(Integer channelId) {
        return TopicEntity.builder()
                .id(id)
                .title(title)
                .channelId(channelId)
                .build();
    }
}

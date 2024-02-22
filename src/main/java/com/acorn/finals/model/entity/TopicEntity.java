package com.acorn.finals.model.entity;

import com.acorn.finals.model.dto.TopicDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.apache.ibatis.type.Alias;

@EqualsAndHashCode(callSuper = false)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Alias("topicEntity")
public class TopicEntity extends BaseEntity {
    Integer id;
    String title;
    Integer channelId;

    public TopicDto toDto() {
        return new TopicDto(id, title);
    }
}

package com.acorn.finals.model.entity;

import com.acorn.finals.model.dto.TopicDto;
import lombok.*;
import org.apache.ibatis.type.Alias;

@EqualsAndHashCode(callSuper = false)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Alias("topicEntity")
public class TopicEntity extends BaseEntity {
    private Integer id;
    private String title;
    private Integer channelId;
    private Integer isRtcChannel;
    public TopicDto toDto() {
        return new TopicDto(id, title, !isRtcChannel.equals(0));
    }
}

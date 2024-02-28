package com.acorn.finals.model.dto;

import com.acorn.finals.model.entity.ChannelEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChannelDto {
    int id;
    String name;
    String thumbnail;
    String inviteCode;

    public ChannelEntity toEntity(Integer channelId) {
        return ChannelEntity.builder()
                .id(channelId)
                .name(name)
                .thumbnail(thumbnail)
                .inviteCode(inviteCode)
                .build();
    }
}

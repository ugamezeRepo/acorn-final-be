package com.acorn.finals.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.ibatis.type.Alias;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Alias("channelMemberEntity")
public class ChannelMemberEntity {
    Integer id;
    Integer channelId;
    Integer memberId;
}


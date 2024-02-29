package com.acorn.finals.model.entity;

import lombok.*;
import org.apache.ibatis.type.Alias;

@EqualsAndHashCode(callSuper = false)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Alias("channelMemberEntity")
public class ChannelMemberEntity extends BaseEntity {
    Integer id;
    Integer channelId;
    Integer memberId;
    String role;
}


package com.acorn.finals.model.dto;

import com.acorn.finals.model.entity.ChannelMemberEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChangeRoleRequestDto {
    Integer id;
    String ownerEmail;
    Integer channelId;
    Integer memberId;
    String newRole;

    public ChannelMemberEntity toEntity() {
        return new ChannelMemberEntity(id, channelId, memberId, newRole);
    }
}

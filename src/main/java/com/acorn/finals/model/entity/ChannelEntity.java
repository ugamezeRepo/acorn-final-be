package com.acorn.finals.model.entity;


import com.acorn.finals.model.dto.ChannelDto;
import lombok.*;
import org.apache.ibatis.type.Alias;


@EqualsAndHashCode(callSuper = false)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Alias("channelEntity")
public class ChannelEntity extends BaseEntity {
    Integer id;
    String name;
    String thumbnail;
    String inviteCode;

    public ChannelDto toDto() {
        return new ChannelDto(id, name, thumbnail, inviteCode);
    }
}

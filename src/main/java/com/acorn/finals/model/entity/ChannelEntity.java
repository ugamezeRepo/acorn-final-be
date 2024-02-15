package com.acorn.finals.model.entity;


import lombok.*;
import org.apache.ibatis.type.Alias;


@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Alias("channelEntity")
public class ChannelEntity extends BaseEntity {
    Integer id;
    String name;
    String thumbnail;
}

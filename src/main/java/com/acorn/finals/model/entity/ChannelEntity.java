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
@Alias("channelEntity")
public class ChannelEntity {
    Integer id;
    String name;
    String thumbnail;
}

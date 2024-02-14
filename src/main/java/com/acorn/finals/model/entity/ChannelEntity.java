package com.acorn.finals.model.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.ibatis.type.Alias;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Alias("channelEntity")
public class ChannelEntity {
    Integer id;
    String channelName;
    String channelThumbnail;
}

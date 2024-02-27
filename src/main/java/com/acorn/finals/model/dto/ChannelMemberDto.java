package com.acorn.finals.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChannelMemberDto {
    @JsonProperty("name")
    String channelName;
    @JsonProperty("thumbnail")
    String channelThumbnail;
    @JsonProperty("nickname")
    String memberNickname;
    @JsonProperty("hashtag")
    String memberHashtag;
}

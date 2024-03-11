package com.acorn.finals.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MemberFriendDto {
    private int friendId;
    private String nickname;
    private Integer hashtag;
}

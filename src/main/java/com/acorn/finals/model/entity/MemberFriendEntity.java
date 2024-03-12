package com.acorn.finals.model.entity;

import com.acorn.finals.model.dto.MemberFriendDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.ibatis.type.Alias;

@Alias("memberfriendEntity")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberFriendEntity {
    private int friendId;
    private String nickname;
    private Integer hashtag;

    public MemberFriendDto toDto(){
         return new MemberFriendDto(friendId, nickname, hashtag);
    }

}

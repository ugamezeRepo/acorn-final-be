package com.acorn.finals.model.entity;

import com.acorn.finals.model.dto.MemberDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.ibatis.type.Alias;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Alias("memberEntity")
public class MemberEntity {
    Integer id;
    String email;
    String nickname;
    Integer hashtag;

    public MemberDto toDto() {
        return new MemberDto(email, nickname, hashtag);
    }
}

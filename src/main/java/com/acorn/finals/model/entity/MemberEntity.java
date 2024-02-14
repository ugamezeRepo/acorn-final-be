package com.acorn.finals.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.ibatis.type.Alias;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Alias("memberEntity")
public class MemberEntity {
    Integer id;
    String email;
    String nickname;
    Integer hashtag;
}

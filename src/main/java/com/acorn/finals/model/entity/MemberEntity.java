package com.acorn.finals.model.entity;

import com.acorn.finals.model.dto.MemberDto;
import lombok.*;
import org.apache.ibatis.type.Alias;

@EqualsAndHashCode(callSuper = false)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Alias("memberEntity")
public class MemberEntity extends BaseEntity {
    Integer id;
    String email;
    String nickname;
    Integer hashtag;
    String status;

    public MemberDto toDto() {
        return new MemberDto(id, email, nickname, hashtag, status);
    }
}

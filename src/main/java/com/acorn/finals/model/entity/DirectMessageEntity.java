package com.acorn.finals.model.entity;

import com.acorn.finals.mapper.MemberMapper;
import com.acorn.finals.model.dto.DirectMessageDto;
import com.acorn.finals.model.dto.MemberDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.apache.ibatis.type.Alias;

@EqualsAndHashCode(callSuper = false)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Alias("directMessageEntity")
public class DirectMessageEntity extends BaseEntity {
    private MemberMapper memberMapper;

    Integer id;
    Integer memberId;
    Integer anotherId;
    Integer active;

    public DirectMessageDto toDto () {
        return new DirectMessageDto(
            id,
            memberId,
            anotherId,
            active
        );
    }
}

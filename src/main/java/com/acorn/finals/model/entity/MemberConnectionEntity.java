package com.acorn.finals.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.apache.ibatis.type.Alias;

@EqualsAndHashCode(callSuper = false)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Alias("memberConnectionEntity")
public class MemberConnectionEntity extends BaseEntity {
    Integer id;
    String nickname;
    Integer hashtag;
    String status;
}

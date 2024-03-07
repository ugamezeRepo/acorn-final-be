package com.acorn.finals.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberDto {
    Integer id;
    String email;
    String nickname;
    Integer hashtag;
    String status;
}

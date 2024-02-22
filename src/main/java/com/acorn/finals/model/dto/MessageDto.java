package com.acorn.finals.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageDto {
    MemberDto author;
    String content;
    LocalDateTime createdAt;
}

package com.acorn.finals.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.apache.ibatis.type.Alias;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Alias("messageDMEntity")
public class MessageDMEntity extends BaseEntity {
    Integer id;
    Integer authorId;
    String content;
    Integer dmId;
}

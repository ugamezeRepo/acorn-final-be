package com.acorn.finals.model.entity;

import com.acorn.finals.model.dto.PersonalTopicDto;
import com.acorn.finals.model.dto.TopicDto;
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
@Alias("personalTopicEntity")
public class PersonalTopicEntity extends BaseEntity {
    Integer id;
    Integer member1Id;
    Integer member2Id;

    public PersonalTopicDto toDto() {
        return new PersonalTopicDto(id, member1Id, member2Id);
    }
}

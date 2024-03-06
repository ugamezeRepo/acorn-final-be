package com.acorn.finals.model.dto;

import com.acorn.finals.model.entity.PersonalTopicEntity;
import com.acorn.finals.model.entity.TopicEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PersonalTopicDto {
    Integer id;
    Integer member1Id;
    Integer member2Id;

    public PersonalTopicEntity toEntity(Integer member1Id, Integer member2Id) {
        return PersonalTopicEntity.builder()
                .id(id)
                .member1Id(member1Id)
                .member2Id(member2Id)
                .build();
    }
}

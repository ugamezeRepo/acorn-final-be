package com.acorn.finals.model.dto;

import com.acorn.finals.model.entity.DirectMessageEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DirectMessageDto {
    Integer id;
    Integer memberId;
    Integer anotherId;
    Integer active;

    public DirectMessageEntity toEntity(int id, int memberId, int anotherId, int active) {
        return DirectMessageEntity.builder()
            .id(id)
            .memberId(memberId)
            .anotherId(anotherId)
            .active(active)
            .build();
    }
}

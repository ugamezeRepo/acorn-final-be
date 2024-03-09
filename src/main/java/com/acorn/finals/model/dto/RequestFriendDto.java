package com.acorn.finals.model.dto;

import com.acorn.finals.model.entity.requestFriendEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequestFriendDto {
    int fromId;
    int toId;

    public requestFriendEntity toEntity() {
        return new requestFriendEntity(null, fromId, toId);
    }
}

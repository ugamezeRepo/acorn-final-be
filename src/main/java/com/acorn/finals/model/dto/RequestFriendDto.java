package com.acorn.finals.model.dto;

import com.acorn.finals.model.entity.RequestFriendEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequestFriendDto {
    int fromId;
    int toId;
    String answer;

    public RequestFriendEntity toEntity() {
        return new RequestFriendEntity(null, fromId, toId);
    }
}

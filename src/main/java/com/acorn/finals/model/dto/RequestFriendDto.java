package com.acorn.finals.model.dto;

import com.acorn.finals.model.entity.RequestFriendEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequestFriendDto {
    int FromId;
    int ToId;

    public RequestFriendEntity toEntity(RequestFriendDto dto) {
        return new RequestFriendEntity(null, dto.FromId, dto.ToId);
    }
}

package com.acorn.finals.model.entity;

import com.acorn.finals.model.dto.RequestFriendDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.ibatis.type.Alias;

@Alias("requestFriendEntity")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class RequestFriendEntity {
    private Integer id;
    private int fromId;
    private int toId;

    public RequestFriendDto toDto() {
        return new RequestFriendDto(fromId, toId);
    }
}

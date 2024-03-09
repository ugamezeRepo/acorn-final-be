package com.acorn.finals.mapper;

import com.acorn.finals.model.entity.RequestFriendEntity;

import java.util.List;

public interface FriendMapper {
    int friendRequestAdd(RequestFriendEntity entity);

    List<RequestFriendEntity> friendRequestList(int toId);

    int deleteRequest(RequestFriendEntity entity);

    List<RequestFriendEntity> isExistedRequest(RequestFriendEntity entity);
}

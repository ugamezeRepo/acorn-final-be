package com.acorn.finals.mapper;

import com.acorn.finals.model.entity.MemberEntity;
import com.acorn.finals.model.entity.RequestFriendEntity;

import java.util.List;
import java.util.Map;

public interface FriendMapper {
    int friendRequestAdd(RequestFriendEntity entity);

    List<MemberEntity> friendRequestList(int toId);

    int deleteRequest(RequestFriendEntity entity);

    List<RequestFriendEntity> isExistedRequest(RequestFriendEntity entity);


    int addFriend(RequestFriendEntity entity);

    int reverseAddFriend(RequestFriendEntity entity);

    List<MemberEntity> friendAllList(int myId);

    List<MemberEntity> findNewFriendWithoutFriend(Map<String, Object> map);
}

package com.acorn.finals.mapper;

import com.acorn.finals.model.entity.MemberEntity;
import com.acorn.finals.model.entity.requestFriendEntity;

import java.util.List;
import java.util.Map;

public interface FriendMapper {
    int friendRequestAdd(requestFriendEntity entity);

    List<MemberEntity> friendRequestList(int toId);

    int deleteRequest(requestFriendEntity entity);

    List<requestFriendEntity> isExistedRequest(requestFriendEntity entity);


    int addFriend(requestFriendEntity entity);

    int reverseAddFriend(requestFriendEntity entity);

    List<MemberEntity> friendAllList(int myId);

    List<MemberEntity> findNewFriendWithoutFriend(Map<String, Object> map);
}

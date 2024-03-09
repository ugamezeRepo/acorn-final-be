package com.acorn.finals.service;

import com.acorn.finals.mapper.FriendMapper;
import com.acorn.finals.model.dto.MemberDto;
import com.acorn.finals.model.dto.RequestFriendDto;
import com.acorn.finals.model.entity.MemberEntity;
import com.acorn.finals.model.entity.requestFriendEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FriendService {
    private final FriendMapper friendMapper;

    @Transactional
    public RequestFriendDto addFriendRequest(requestFriendEntity entity) {

        if (friendMapper.isExistedRequest(entity) != null) {
            return null;
        }
        friendMapper.friendRequestAdd(entity);
        return entity.toDto();
    }

    public List<RequestFriendDto> friendRequestList(RequestFriendDto dto) {
        List<requestFriendEntity> list = friendMapper.friendRequestList(dto.getToId());

        return list.stream()
                .map(requestFriendEntity::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public boolean friendListAnswerAndDelete(RequestFriendDto dto) {
        requestFriendEntity entity = dto.toEntity();
        int deletedRow = friendMapper.deleteRequest(entity);
        if (friendMapper.addFriend(entity) > 0) {
            friendMapper.reverseAddFriend(entity);
        }
        return deletedRow > 0;
    }

    public List<MemberDto> friendList(int myId) {
        List<MemberEntity> entity = friendMapper.friendAllList(myId);
        return entity.stream()
                .map(MemberEntity::toDto)
                .collect(Collectors.toList());
    }
}

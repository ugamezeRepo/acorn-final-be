package com.acorn.finals.service;

import com.acorn.finals.mapper.FriendMapper;
import com.acorn.finals.model.dto.MemberDto;
import com.acorn.finals.model.dto.RequestFriendDto;
import com.acorn.finals.model.entity.MemberEntity;
import com.acorn.finals.model.entity.RequestFriendEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FriendService {
    private final FriendMapper friendMapper;

    @Transactional
    public boolean addFriendRequest(RequestFriendEntity entity) {
        if (!friendMapper.isExistedRequest(entity).isEmpty()) {
            return false;
        }
        friendMapper.friendRequestAdd(entity);
        return true;
    }

    public List<MemberDto> friendRequestList(RequestFriendDto dto) {
        List<MemberEntity> list = friendMapper.friendRequestList(dto.getToId());

        return list.stream()
                .map(MemberEntity::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public boolean friendListAnswerAndDelete(RequestFriendDto dto) {
        RequestFriendEntity entity = dto.toEntity();
        int deletedRow = friendMapper.deleteRequest(entity);
        if (dto.getAnswer().equals("yes")) {
            friendMapper.addFriend(entity);
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

    public List<MemberDto> findNewFriend(Map<String, Object> map) {
        List<MemberEntity> entity = friendMapper.findNewFriendWithoutFriend(map);
        return entity.stream()
                .map(MemberEntity::toDto)
                .collect(Collectors.toList());
    }
}

package com.acorn.finals.service;

import com.acorn.finals.mapper.FriendMapper;
import com.acorn.finals.model.dto.RequestFriendDto;
import com.acorn.finals.model.entity.RequestFriendEntity;
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
    public RequestFriendDto addFriendRequest(RequestFriendEntity entity) {

        if (friendMapper.isExistedRequest(entity) != null) {
            return null;
        }
        friendMapper.friendRequestAdd(entity);
        return entity.toDto();
    }

    public List<RequestFriendDto> friendRequestList(RequestFriendDto dto) {
        List<RequestFriendEntity> list = friendMapper.friendRequestList(dto.getToId());

        return list.stream()
                .map(RequestFriendEntity::toDto)
                .collect(Collectors.toList());
    }

    public boolean friendListAnswerAndDelete(RequestFriendDto dto) {
        int deletedRow = friendMapper.deleteRequest(dto.toEntity());
        return deletedRow > 0;
    }
}

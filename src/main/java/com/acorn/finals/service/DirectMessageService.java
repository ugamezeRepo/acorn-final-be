package com.acorn.finals.service;

import com.acorn.finals.mapper.DirectMessageMapper;
import com.acorn.finals.mapper.MemberMapper;
import com.acorn.finals.model.dto.DirectMessageDto;
import com.acorn.finals.model.entity.DirectMessageEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DirectMessageService {
    private final DirectMessageMapper directMessageMapper;
    private final MemberMapper memberMapper;

    @Transactional
    public List<DirectMessageDto> findAllActiveDM(Integer memberId) {
        var entities = directMessageMapper.findAllActiveByMemberId(memberId);

        return entities.stream()
                .map(DirectMessageEntity::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public DirectMessageDto findOneById(int id) {
        var entity = directMessageMapper.findOneById(id);

        // TODO:  entity 가 있는지 없는지 확인을 할 것
        if (entity.getActive() == 0) {
            directMessageMapper.changeDirectMessageActivation(entity);
        }
        return entity.toDto();
    }

    @Transactional
    public DirectMessageDto findOneByMemberIdAndAnotherId(int memberId, int anotherId) {
        var entity = directMessageMapper.findOneByMemberIdAndAnotherId(memberId, anotherId);

        return entity.toDto();
    }

    @Transactional
    public DirectMessageDto createNewDM(DirectMessageDto directMessageDto, Authentication auth) {
        var memberId = Integer.parseInt(auth.getName());
        int anotherId = directMessageDto.getAnotherId();

        var directMessageEntity = directMessageDto.toEntity(0, memberId, anotherId, 1);
        directMessageMapper.insert(directMessageEntity);

        return directMessageEntity.toDto();
    }

    @Transactional
    public DirectMessageDto activateDM(int id, DirectMessageDto directMessageDMActivateRequest) {
        var entity = directMessageMapper.findOneById(id);
        entity.setActive(directMessageDMActivateRequest.getActive());
        directMessageMapper.changeDirectMessageActivation(entity);

        return entity.toDto();
    }

    @Transactional
    public boolean deleteDM(int directMessage) {
        return directMessageMapper.deleteById(directMessage) > 0;
    }

}

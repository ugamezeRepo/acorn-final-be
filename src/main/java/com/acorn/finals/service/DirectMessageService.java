package com.acorn.finals.service;

import com.acorn.finals.mapper.MemberMapper;
import com.acorn.finals.mapper.DirectMessageMapper;
import com.acorn.finals.model.dto.DirectMessageDto;
import com.acorn.finals.model.entity.DirectMessageEntity;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DirectMessageService {
    private final DirectMessageMapper directMessageMapper;
    private final MemberMapper memberMapper;

    @Transactional
    public List<DirectMessageDto> findAllActiveDM(String email) {
        int memberId = memberMapper.findOneByEmail(email).getId();
        var entities = directMessageMapper.findAllActiveByMemberId(memberId);

        return entities.stream()
            .map(DirectMessageEntity::toDto)
            .collect(Collectors.toList());
    }

    @Transactional
    public DirectMessageDto findOneById(int id) {
        var entity = directMessageMapper.findOneById(id);
        if (entity.getActive() == 0) {
            directMessageMapper.activateDM(entity);
        }

        return entity.toDto();
    }

    @Transactional
    public DirectMessageDto createNewDM(DirectMessageDto directMessageDto, Authentication auth) {
        int memberId = memberMapper.findOneByEmail(auth.getName()).getId();
        int anotherId = directMessageDto.getAnotherId();

        var directMessageEntity = directMessageDto.toEntity(0, memberId, anotherId, 1);
        directMessageMapper.insert(directMessageEntity);

        return directMessageEntity.toDto();
    }

    @Transactional
    public DirectMessageDto activateDM(int id, DirectMessageDto directMessageDMActivateRequest) {
        var entity = directMessageMapper.findOneById(id);
        entity.setActive(directMessageDMActivateRequest.getActive());
        directMessageMapper.activateDM(entity);

        return entity.toDto();
    }

    @Transactional
    public boolean deleteDM(int directMessage) {
        return directMessageMapper.deleteById(directMessage) > 0;
    }

}

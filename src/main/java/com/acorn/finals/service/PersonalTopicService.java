package com.acorn.finals.service;

import com.acorn.finals.mapper.MemberMapper;
import com.acorn.finals.mapper.PersonalTopicMapper;
import com.acorn.finals.model.dto.PersonalTopicDto;
import com.acorn.finals.model.entity.PersonalTopicEntity;
import java.lang.reflect.Member;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PersonalTopicService {
    private final PersonalTopicMapper personalTopicMapper;
    private final MemberMapper memberMapper;

    public List<PersonalTopicDto> findAllByMemberId(Authentication auth) {
        int member1Id = memberMapper.findOneByEmail(auth.getName()).getId();
        var entities = personalTopicMapper.findAllByMemberId(member1Id);

        return entities.stream()
            .map(PersonalTopicEntity::toDto)
            .collect(Collectors.toList());
    }

    public PersonalTopicDto findOneByPersonalTopicId(int personalTopicId) {
        var entities = personalTopicMapper.findOneById(personalTopicId);

        return entities.toDto();
    }

    @Transactional
    public PersonalTopicDto createNewTopic(PersonalTopicDto personalTopicCreateRequest, Authentication auth) {
        int member1Id = memberMapper.findOneByEmail(auth.getName()).getId();
        int member2Id = personalTopicCreateRequest.getMember2Id();
        var personalTopicEntity = personalTopicCreateRequest.toEntity(member1Id, member2Id);
        personalTopicMapper.insert(personalTopicEntity);

        return personalTopicEntity.toDto();
    }

    @Transactional
    public boolean removePersonalTopic(int personalTopicId) {
        return personalTopicMapper.deleteById(personalTopicId) > 0;
    }

}

package com.acorn.finals.service;

import com.acorn.finals.mapper.ChannelMemberMapper;
import com.acorn.finals.mapper.MemberMapper;
import com.acorn.finals.model.dto.MemberDto;
import com.acorn.finals.model.entity.ChannelEntity;
import com.acorn.finals.model.entity.MemberEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberService {
    private final MemberMapper memberMapper;
    private final ChannelMemberMapper channelMemberMapper;

    public List<MemberDto> findAllMemberByChannelId(int channelId) {
        return memberMapper.findAllByChannelId(channelId).stream()
                .map(MemberEntity::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public boolean updateStatus(MemberDto dto) {
        var memberEntity = memberMapper.findOneByNicknameAndHashtag(dto.getNickname(), dto.getHashtag());
        log.debug("find one by nickname and hashtag done");
        if (memberEntity == null) {
            return false;
        }
        memberEntity.setStatus(dto.getStatus());
        return memberMapper.update(memberEntity) > 0;
    }

    @Transactional
    public List<Integer> findAllJoinedChannelIdByMember(MemberDto dto) {
        var memberEntity = memberMapper.findOneByNicknameAndHashtag(dto.getNickname(), dto.getHashtag());
        if (memberEntity == null) {
            return List.of();
        }
        return channelMemberMapper
                .findAllChannelByMemberId(memberEntity.getId()).stream()
                .map(ChannelEntity::getId)
                .toList();
    }

    @Transactional
    public MemberDto changeNickandTag(MemberDto dto) {
        var memberEntity = memberMapper.findOneByEmail(dto.getEmail());
        if (memberEntity == null) {
            return new MemberDto();
        }
        if (dto.getNickname() != null) {
            memberEntity.setNickname(dto.getNickname());
        } else if (dto.getHashtag() != null) {
            memberEntity.setHashtag(dto.getHashtag());
        }
        memberMapper.update(memberEntity);

        return memberEntity.toDto();
    }
}

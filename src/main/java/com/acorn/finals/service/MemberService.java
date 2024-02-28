package com.acorn.finals.service;

import com.acorn.finals.mapper.ChannelMemberMapper;
import com.acorn.finals.mapper.MemberMapper;
import com.acorn.finals.model.dto.ChannelDto;
import com.acorn.finals.model.dto.MemberDto;
import com.acorn.finals.model.entity.ChannelEntity;
import com.acorn.finals.model.entity.MemberEntity;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberService {
    private final MemberMapper memberMapper;
    private final ChannelMemberMapper channelMemberMapper;

    public MemberDto findMemberByEmail(String email) {
        var memberEntity = memberMapper.findOneByEmail(email);
        return memberEntity.toDto();
    }

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

    @Transactional
    public List<ChannelDto> listAllChannels(MemberDto member) {
        var memberEntity = memberMapper.findOneByNicknameAndHashtag(member.getNickname(), member.getHashtag());
        var channels = channelMemberMapper.findAllChannelByMemberId(memberEntity.getId());
        return channels.stream()
                .map(ChannelEntity::toDto)
                .toList();
    }

    @Transactional
    public List<ChannelDto> listAllChannelsByEmail(String email) {
        var memberEntity = memberMapper.findOneByEmail(email);
        var channels = channelMemberMapper.findAllChannelByMemberId(memberEntity.getId());
        return channels.stream()
                .map(ChannelEntity::toDto)
                .toList();
    }


    public boolean signup(MemberEntity entity) {
        boolean isSuccess = false;
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        if (memberMapper.findOneByEmail(userEmail) == null) {
            entity.setHashtag(entity.getHashtag());
            entity.setNickname(entity.getNickname());
            entity.setEmail(userEmail);
            entity.setStatus("offline");
            memberMapper.insert(entity);
            isSuccess = true;
        }
        return isSuccess;
    }

}

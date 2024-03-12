package com.acorn.finals.service;

import com.acorn.finals.mapper.ChannelMapper;
import com.acorn.finals.mapper.ChannelMemberMapper;
import com.acorn.finals.mapper.MemberMapper;
import com.acorn.finals.model.dto.ChannelDto;
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
    private final TokenService tokenService;
    private final ChannelMapper channelMapper;

    public MemberDto findMemberById(int id) {
        var memberEntity = memberMapper.findOneById(id);
        return memberEntity.toDto();
    }

    public List<MemberDto> findAllMemberByChannelId(int channelId) {
        return memberMapper.findAllByChannelId(channelId).stream().map(MemberEntity::toDto).collect(Collectors.toList());
    }

    @Transactional
    public void updateStatus(MemberDto dto) {
        if (dto.getNickname() == null || dto.getHashtag() == null) {
            return;
        }
        var memberEntity = memberMapper.findOneById(dto.getId());
        if (memberEntity == null) {
            return;
        }
        memberEntity.setStatus(dto.getStatus());
        memberMapper.update(memberEntity);
    }

    @Transactional
    public List<Integer> findAllJoinedChannelIdByMemberId(int memberId) {
        return channelMemberMapper.findAllChannelByMemberId(memberId).stream().map(ChannelEntity::getId).toList();
    }

    @Transactional
    public List<Integer> findAllJoinedChannelIdByMember(MemberDto dto) {
        if (dto.getNickname() == null || dto.getHashtag() == null) {
            return List.of();
        }
        return channelMemberMapper.findAllChannelByMemberId(dto.getId()).stream().map(ChannelEntity::getId).toList();
    }

    @Transactional
    public MemberDto changeNickandTag(MemberDto dto) {
        var memberEntity = memberMapper.findOneByEmail(dto.getEmail());
        if (memberEntity == null) {
            return new MemberDto();
        }
        if (dto.getNickname() != null) {
            memberEntity.setNickname(dto.getNickname());
        }
        if (dto.getHashtag() != null) {
            memberEntity.setHashtag(dto.getHashtag());
        }
        memberMapper.update(memberEntity);

        return memberEntity.toDto();
    }

    @Transactional
    public List<ChannelDto> listAllChannels(MemberDto member) {
        var channels = channelMemberMapper.findAllChannelByMemberId(member.getId());
        return channels.stream().map(ChannelEntity::toDto).toList();
    }

    @Transactional
    public List<ChannelDto> listAllChannelsByMemberId(Integer memberId) {
        var channels = channelMemberMapper.findAllChannelByMemberId(memberId);
        return channels.stream().map(ChannelEntity::toDto).toList();
    }

    @Transactional
    public boolean signup(MemberEntity entity) {
        boolean isSuccess = false;
        try {
            // TODO: 중복 닉 + 해시태그는 막도록 하기
//
//            if (memberMapper.findOneById(entity.getId()) != null) {
//                throw new RuntimeException("중복된 아이디 존재");
//            }
            if (memberMapper.findOneByEmail(entity.getEmail()) == null) {
                memberMapper.insert(entity);
                isSuccess = true;
            }
        } catch (Exception e) {
            log.debug("{}", e.getMessage());
        }

        return isSuccess;
    }

    @Transactional
    public String getMemberChannelRole(Integer memberId, int channelId) {
        return channelMemberMapper.findRoleByMemberIdAndChannelId(memberId, channelId);
    }
}

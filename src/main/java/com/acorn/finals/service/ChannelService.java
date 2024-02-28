package com.acorn.finals.service;

import com.acorn.finals.mapper.ChannelMapper;
import com.acorn.finals.mapper.ChannelMemberMapper;
import com.acorn.finals.mapper.MemberMapper;
import com.acorn.finals.mapper.TopicMapper;
import com.acorn.finals.model.dto.ChannelDto;
import com.acorn.finals.model.dto.MemberDto;
import com.acorn.finals.model.entity.ChannelEntity;
import com.acorn.finals.model.entity.ChannelMemberEntity;
import com.acorn.finals.model.entity.MemberEntity;
import com.acorn.finals.model.entity.TopicEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChannelService {
    private final ChannelMapper channelMapper;
    private final MemberMapper memberMapper;
    private final ChannelMemberMapper channelMemberMapper;
    private final TopicMapper topicMapper;


    public ChannelDto findChannelInfoByInviteCode(String inviteCode) {
        ChannelEntity entity = channelMapper.findOneByInviteCode(inviteCode);
        return entity.toDto();
    }

    public String generateChannelUrl(int channelId) {
        return String.format("/channel/%d", channelId);
    }

    public List<ChannelDto> listAllChannels() {
        var entities = channelMapper.findAll();
        return entities.stream()
                .map(ChannelEntity::toDto)
                .collect(Collectors.toList());
    }

    public ChannelDto findChannelById(int channelId) {
        var entity = channelMapper.findOneById(channelId);
        return entity.toDto();
    }

    @Transactional
    public ChannelDto createNewChannel(ChannelDto channelCreateRequest, Authentication auth) {
        String inviteCode = UUID.randomUUID().toString();
        channelCreateRequest.setInviteCode(inviteCode);
        ChannelDto channelDto = new ChannelDto(0, channelCreateRequest.getName(), channelCreateRequest.getThumbnail(), channelCreateRequest.getInviteCode());
        var channelEntity = channelDto.toEntity(null);
        channelMapper.insert(channelEntity);

        var memberEntity = memberMapper.findOneByEmail(auth.getName());
        var channelMemberEntity = new ChannelMemberEntity(null, channelEntity.getId(), memberEntity.getId());
        channelMemberMapper.insert(channelMemberEntity);

        var topicEntity = new TopicEntity();
        topicEntity.setTitle("일반");
        topicEntity.setChannelId(channelEntity.getId());
        topicMapper.insert(topicEntity);

        return channelEntity.toDto();
    }

    public ChannelDto updateChannel(int channelId, ChannelDto channelUpdateRequest) {
        var channelEntity = channelUpdateRequest.toEntity(channelId);
        if (channelMapper.update(channelEntity) == 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        return channelUpdateRequest;
    }

    public boolean deleteChannel(int channelId) {
        return channelMapper.deleteById(channelId) > 0;
    }

    public List<MemberDto> listChannelMembers(int channelId) {
        List<MemberEntity> entities = channelMemberMapper.findAllMemberByChannelId(channelId);
        return entities.stream()
                .map(MemberEntity::toDto)
                .collect(Collectors.toList());
    }

}

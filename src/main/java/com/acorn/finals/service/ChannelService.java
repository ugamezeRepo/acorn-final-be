package com.acorn.finals.service;

import com.acorn.finals.mapper.ChannelMapper;
import com.acorn.finals.mapper.ChannelMemberMapper;
import com.acorn.finals.model.dto.ChannelDto;
import com.acorn.finals.model.dto.MemberDto;
import com.acorn.finals.model.entity.ChannelEntity;
import com.acorn.finals.model.entity.MemberEntity;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class ChannelService {
    private final ChannelMapper channelMapper;
    private final ChannelMemberMapper channelMemberMapper;

    String generateChannelUrl(int channelId) {
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

    public ChannelDto createNewChannel(ChannelDto channelCreateRequest) {
        var channelEntity = channelCreateRequest.toEntity(null);
        channelMapper.insert(channelEntity);
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

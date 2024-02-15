package com.acorn.finals.service;

import com.acorn.finals.mapper.ChannelMapper;
import com.acorn.finals.mapper.ChannelMemberMapper;
import com.acorn.finals.model.UrlResponse;
import com.acorn.finals.model.dto.ChannelDto;
import com.acorn.finals.model.dto.MemberDto;

import java.util.List;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class ChannelService {
    private final CryptoService cryptoService;
    private final ChannelMapper channelMapper;
    private final ChannelMemberMapper channelMemberMapper;

    String generateChannelUrl(int channelId) {
        return String.format("/channel/%d", channelId);
    }

    public UrlResponse<ChannelDto> createNewChannel(ChannelDto channelCreateRequest) {
        var channelEntity = channelCreateRequest.toEntity(null);
        channelMapper.insert(channelEntity);
        int id = channelEntity.getId();
        String url = generateChannelUrl(id);
        return new UrlResponse<>(url, channelCreateRequest);
    }

    public ChannelDto updateChannel(int channelId, ChannelDto channelUpdateRequest) {
        var channelEntity = channelUpdateRequest.toEntity(channelId);
        if (channelMapper.update(channelEntity) == 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        return channelUpdateRequest;
    }

    public List<MemberDto> listChannelMembers(int channelId) {
        var entities = channelMemberMapper.findAllMemberByChannelId(channelId);
        return entities.stream()
                .map(entity -> new MemberDto(entity.getEmail(), entity.getNickname(), entity.getHashtag()))
                .collect(Collectors.toList());
    }
}

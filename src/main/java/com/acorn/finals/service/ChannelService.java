package com.acorn.finals.service;

import com.acorn.finals.model.UrlResponse;
import com.acorn.finals.model.dto.ChannelDto;
import com.acorn.finals.model.entity.ChannelEntity;
import com.acorn.finals.repository.ChannelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;

@Service
@RequiredArgsConstructor
public class ChannelService {
    private final CryptoService cryptoService;
    private final ChannelRepository channelRepository;

    public UrlResponse<ChannelDto> createNewChannel(ChannelDto channelCreateDto) throws NoSuchAlgorithmException {
        var channelEntity = ChannelEntity.builder()
                                .name(channelCreateDto.getName())
                                .thumbnail(channelCreateDto.getThumbnail())
                                .build();

        int id = channelRepository.insert(channelEntity);
        String url = "/channel/" + cryptoService.encode(id);
        return new UrlResponse<>(url, channelCreateDto);
    }
}

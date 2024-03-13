package com.acorn.finals.service;

import com.acorn.finals.mapper.MemberMapper;
import com.acorn.finals.mapper.MessageChannelMapper;
import com.acorn.finals.model.dto.MemberDto;
import com.acorn.finals.model.dto.MessageDto;
import com.acorn.finals.model.entity.MessageChannelEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MessageChannelService {
    private final MemberMapper memberMapper;
    private final MessageChannelMapper messageChannelMapper;

    public List<MessageDto> findAllByChannelIdAndTopicId(int channelId, int topicId) {
        var entities = messageChannelMapper.findAllMessageByChannelIdAndTopicId(channelId, topicId);
        return entities.stream()
                .map(entity -> {
                    MemberDto author = memberMapper.findOneById(entity.getAuthorId()).toDto();
                    return new MessageDto(entity.getId(), author, entity.getContent(), entity.getCreatedAt());
                })
                .collect(Collectors.toList());
    }

    @Transactional
    public MessageDto insertMsg(MessageDto dto, int channelId, int topicId) {
        var tmpAuthor = dto.getAuthor();
        var authorEntity = memberMapper.findOneById(tmpAuthor.getId());
        var messageEntity = new MessageChannelEntity(null, authorEntity.getId(), dto.getContent(), channelId, topicId);
        messageChannelMapper.insert(messageEntity);
        return new MessageDto(messageEntity.getId(), authorEntity.toDto(), messageEntity.getContent(),
                messageEntity.getCreatedAt());
    }

    @Transactional
    public int updateMsg(MessageDto dto) {
        var messageEntity = messageChannelMapper.findOneById(dto.getId());
        messageEntity.setContent(dto.getContent());
        dto.setCreatedAt(messageEntity.getCreatedAt());
        return messageChannelMapper.update(messageEntity);
    }

    @Transactional
    public int deleteMsg(MessageDto dto) {
        return messageChannelMapper.deleteById(dto.getId());
    }
}

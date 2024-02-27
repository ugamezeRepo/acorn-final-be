package com.acorn.finals.service;

import com.acorn.finals.mapper.MemberMapper;
import com.acorn.finals.mapper.MessageMapper;
import com.acorn.finals.model.dto.MemberDto;
import com.acorn.finals.model.dto.MessageDto;
import com.acorn.finals.model.entity.MessageEntity;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MessageService {
    private final MemberMapper memberMapper;
    private final MessageMapper messageMapper;

    public List<MessageDto> findAllByChannelIdAndTopicId(int channelId, int topicId) {
        var entities = messageMapper.findAllMessageByChannelIdAndTopicId(channelId, topicId);
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
        var authorEntity = memberMapper.findOneByNicknameAndHashtag(tmpAuthor.getNickname(), tmpAuthor.getHashtag());
        var messageEntity = new MessageEntity(null, authorEntity.getId(), dto.getContent(), channelId, topicId);
        messageMapper.insert(messageEntity);
        return new MessageDto(messageEntity.getId(), authorEntity.toDto(), messageEntity.getContent(),
                messageEntity.getCreatedAt());
    }

    @Transactional
    public int updateMsg(MessageDto dto) {
        var messageEntity = messageMapper.findOneById(dto.getId());
        messageEntity.setContent(dto.getContent());
        dto.setCreatedAt(messageEntity.getCreatedAt());
        return messageMapper.update(messageEntity);
    }

    @Transactional
    public int deleteMsg(MessageDto dto) {
        return messageMapper.deleteById(dto.getId());
    }
}

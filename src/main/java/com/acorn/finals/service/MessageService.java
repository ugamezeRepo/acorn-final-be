package com.acorn.finals.service;

import com.acorn.finals.mapper.MemberMapper;
import com.acorn.finals.mapper.MessageMapper;
import com.acorn.finals.model.dto.MemberDto;
import com.acorn.finals.model.dto.MessageDto;
import com.acorn.finals.model.entity.MessageEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

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
                    return new MessageDto(author, entity.getContent(), entity.getCreatedAt());
                })
                .collect(Collectors.toList());
    }
//
//    public int newMessageAdd(MessageEntity entity) {
//        return messageMapper.insert(entity);
//
//    }

    @Transactional
    public MessageDto insertMsg(MessageDto dto, int channelId, int topicId) {
        var tmpAuthor = dto.getAuthor();
        var authorEntity = memberMapper.findOneByNicknameAndHashtag(tmpAuthor.getNickname(), tmpAuthor.getHashtag());
        var messageEntity = new MessageEntity(null, authorEntity.getId(), dto.getContent(), channelId, topicId);
        messageMapper.insert(messageEntity);
        return new MessageDto(authorEntity.toDto(), messageEntity.getContent(), messageEntity.getCreatedAt());
    }

    @Transactional
    public MessageDto updateMsg(MessageDto dto, int channelId, int topicId) {
        var tmpAuthor = dto.getAuthor();
        var authorEntity = memberMapper.findOneByNicknameAndHashtag(tmpAuthor.getNickname(), tmpAuthor.getHashtag());
        var messageEntity = new MessageEntity(null, authorEntity.getId(), dto.getContent(), channelId, topicId);
        messageMapper.update(messageEntity);

        return null;
    }

    @Transactional
    public int deleteMsg() {
        System.out.println("DELETE Service");
//        messageMapper.deleteLastId();
        return 0;
    }
}

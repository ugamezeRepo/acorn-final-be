package com.acorn.finals.service;

import com.acorn.finals.mapper.MemberMapper;
import com.acorn.finals.mapper.MessageMapper;
import com.acorn.finals.model.dto.MemberDto;
import com.acorn.finals.model.dto.MessageDto;
import com.acorn.finals.model.entity.MessageEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
    public int newMessageAdd(MessageEntity entity){
        return messageMapper.insert(entity);

    }

    public MessageDto receviedAndSend(MessageEntity entity){
        MemberDto author = memberMapper.findOneById(entity.getAuthorId()).toDto();



        return new MessageDto(author, entity.getContent(), entity.getCreatedAt());
    }
}

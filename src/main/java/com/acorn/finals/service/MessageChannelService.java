package com.acorn.finals.service;

import com.acorn.finals.mapper.MemberMapper;
import com.acorn.finals.mapper.MessageChannelMapper;
import com.acorn.finals.model.dto.MemberDto;
import com.acorn.finals.model.dto.MessageDto;
import com.acorn.finals.model.entity.MessageChannelEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
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

    public MessageDto findOneById(int id) {
        var entity = messageChannelMapper.findOneById(id);
        MemberDto author = memberMapper.findOneById(entity.getAuthorId()).toDto();
        return new MessageDto(entity.getId(), author, entity.getContent(), entity.getCreatedAt());
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

        int requestAuthorId = dto.getAuthor().getId();
        int messageAuthorId = messageEntity.getAuthorId();
        if (requestAuthorId != messageAuthorId) {
            log.info("메세지 수정 권한 없음! 메세지 작성자가 아닙니다.");
            return -1;
        }

        messageEntity.setContent(dto.getContent());
        dto.setCreatedAt(messageEntity.getCreatedAt());
        return messageChannelMapper.update(messageEntity);
    }

    @Transactional
    public int deleteMsg(MessageDto dto) {
        int requestAuthorId = dto.getAuthor().getId();
        int messageAuthorId = messageChannelMapper.findOneById(dto.getId()).getAuthorId();
        if (requestAuthorId != messageAuthorId) {
            log.info("메세지 삭제 권한 없음! 메세지 작성자가 아닙니다.");
            return -1;
        }

        return messageChannelMapper.deleteById(dto.getId());
    }
}

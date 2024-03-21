package com.acorn.finals.service;

import com.acorn.finals.mapper.MemberMapper;
import com.acorn.finals.mapper.MessageDMMapper;
import com.acorn.finals.model.dto.MemberDto;
import com.acorn.finals.model.dto.MessageDto;
import com.acorn.finals.model.entity.MessageDMEntity;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class MessageDMService {
    private final MemberMapper memberMapper;
    private final MessageDMMapper messageDMMapper;

    public List<MessageDto> findAllByDMId(int dmId) {
        var messageEntities = messageDMMapper.findAllMessageByDMId(dmId);

        return messageEntities.stream()
                .map(entity -> {
                    MemberDto author = memberMapper.findOneById(entity.getAuthorId()).toDto();
                    return new MessageDto(entity.getId(), author, entity.getContent(), entity.getCreatedAt());
                })
                .collect(Collectors.toList());
    }

    @Transactional
    public MessageDto insertMsg(MessageDto dto, int dmId) {
        var authorEntity = memberMapper.findOneById(dto.getAuthor().getId());
        var messageEntity = new MessageDMEntity(null, authorEntity.getId(), dto.getContent(), dmId);
        messageDMMapper.insert(messageEntity);

        return new MessageDto(messageEntity.getId(), authorEntity.toDto(), messageEntity.getContent(),
            messageEntity.getCreatedAt());
    }

    @Transactional
    public int updateMsg(MessageDto dto) {
        var messageEntity = messageDMMapper.findOneById(dto.getId());

        int requestAuthorId = dto.getAuthor().getId();
        int messageAuthorId = messageEntity.getAuthorId();
        if (requestAuthorId != messageAuthorId) {
            log.info("메세지 수정 권한 없음! 메세지 작성자가 아닙니다.");
            return -1;
        }
        messageEntity.setContent(dto.getContent());
        dto.setCreatedAt(messageEntity.getCreatedAt());

        return messageDMMapper.update(messageEntity);
    }

    @Transactional
    public int deleteMsg(MessageDto dto) {
        int requestAuthorId = dto.getAuthor().getId();
        int messageAuthorId = messageDMMapper.findOneById(dto.getId()).getAuthorId();
        if (requestAuthorId != messageAuthorId) {
            log.info("메세지 삭제 권한 없음! 메세지 작성자가 아닙니다.");
            return -1;
        }

        return messageDMMapper.deleteById(dto.getId());
    }
}

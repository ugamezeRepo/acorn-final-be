package com.acorn.finals.service;

import com.acorn.finals.mapper.TopicMapper;
import com.acorn.finals.model.dto.TopicDto;
import com.acorn.finals.model.entity.TopicEntity;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TopicService {
    private final TopicMapper topicMapper;
    
    public List<TopicDto> findAllByChannelId(int channelId) {
        var entities = topicMapper.findAllTopicByChannelId(channelId);
        return entities.stream()
                .map(TopicEntity::toDto)
                .collect(Collectors.toList());
    }

    public TopicDto createNewTopic(int channelId, TopicDto topicCreateRequest) {
        var topicEntity = topicCreateRequest.toEntity(channelId);
        topicMapper.insert(topicEntity);
        int id = topicEntity.getId();
        return new TopicDto(id, topicCreateRequest.getTitle());
    }

    public boolean removeTopic(int channelId, int topicId) {
        return topicMapper.delete(channelId, topicId) > 0;
    }

    public boolean updateTopic(int channelId, int topicId, TopicDto topicUpdateRequest) {
        var topicEntity = topicUpdateRequest.toEntity(channelId);
        topicEntity.setId(topicId);
        return topicMapper.update(topicEntity) > 0;
    }
}

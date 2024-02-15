package com.acorn.finals.service;

import com.acorn.finals.mapper.TopicMapper;
import com.acorn.finals.model.UrlResponse;
import com.acorn.finals.model.dto.TopicDto;

import java.util.List;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TopicService {
    private final TopicMapper topicMapper;

    String generateTopicUrl(int channelId, int topicId) {
        return String.format("/channel/%d/topic/%d", channelId, topicId);
    }

    public List<UrlResponse<TopicDto>> findAllByChannelId(int channelId) {
        var entities = topicMapper.findAllTopicByChannelId(channelId);
        return entities.stream()
                .map(entity -> {
                    var url = generateTopicUrl(entity.getChannelId(), entity.getId());
                    var dto = new TopicDto(entity.getTitle());
                    return new UrlResponse<>(url, dto);
                })
                .collect(Collectors.toList());
    }

    public UrlResponse<TopicDto> createNewTopic(int channelId, TopicDto topicCreateRequest) {
        var topicEntity = topicCreateRequest.toEntity(channelId, null);
        topicMapper.insert(topicEntity);
        int id = topicEntity.getId();
        var url = generateTopicUrl(channelId, id);
        return new UrlResponse<>(url, topicCreateRequest);
    }

    public boolean removeTopic(int topicId, TopicDto topicRemoveRequest) {
        return topicMapper.deleteById(topicId) > 0;
    }

    public boolean updateTopic(int channelId, int topicId, TopicDto topicUpdateRequest) {
        var topicEntity = topicUpdateRequest.toEntity(channelId, topicId);
        return topicMapper.update(topicEntity) > 0;
    }
}

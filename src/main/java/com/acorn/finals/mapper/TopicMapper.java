package com.acorn.finals.mapper;

import com.acorn.finals.model.entity.TopicEntity;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TopicMapper {
    List<TopicEntity> findAll();

    TopicEntity findOneById(int id);

    List<TopicEntity> findAllTopicByChannelId(int channelId);

    int insert(TopicEntity entity);

    int update(TopicEntity entity);

    int delete(int channelId, int topicId);

}

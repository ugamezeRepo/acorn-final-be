package com.acorn.finals.mapper;

import com.acorn.finals.model.entity.MessageChannelEntity;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MessageChannelMapper {

    List<MessageChannelEntity> findAll();

    MessageChannelEntity findOneById(int id);

    List<MessageChannelEntity> findAllMessageByChannelIdAndTopicId(int channelId, int topicId);

    int insert(MessageChannelEntity entity);

    int update(MessageChannelEntity entity);

    int deleteById(int id);
}

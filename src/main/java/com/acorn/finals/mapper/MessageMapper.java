package com.acorn.finals.mapper;

import com.acorn.finals.model.entity.MessageEntity;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MessageMapper {
    List<MessageEntity> findAll();

    MessageEntity findOneById(int id);

    List<MessageEntity> findAllMessageByChannelIdAndTopicId(int channelId, int topicId);

    int insert(MessageEntity entity);

    int update(MessageEntity entity);

    int deleteById(int id);
}

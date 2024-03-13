package com.acorn.finals.mapper;

import com.acorn.finals.model.entity.MessageDMEntity;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MessageDMMapper {
    List<MessageDMEntity> findAllMessageByDMId(int dmId);

    MessageDMEntity findOneById(int id);

    int insert(MessageDMEntity entity);

    int update(MessageDMEntity entity);

    int deleteById(int id);
}

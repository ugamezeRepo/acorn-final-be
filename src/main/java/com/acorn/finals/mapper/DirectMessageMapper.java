package com.acorn.finals.mapper;

import com.acorn.finals.model.entity.DirectMessageEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DirectMessageMapper {

    List<DirectMessageEntity> findAllActiveByMemberId(int memberId);

    DirectMessageEntity findOneById(int id);

    int insert(DirectMessageEntity entity);

    int changeDirectMessageActivation(DirectMessageEntity entity);

    int deleteById(int id);

}

package com.acorn.finals.mapper;

import com.acorn.finals.model.entity.ChannelEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ChannelMapper {
    List<ChannelEntity> findAll();

    ChannelEntity findOneById(int id);

    int insert(ChannelEntity entity);

    int update(ChannelEntity entity);

    int deleteById(int id);

    ChannelEntity findOneByInviteCode(String inviteCode);
}

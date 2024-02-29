package com.acorn.finals.mapper;

import com.acorn.finals.model.entity.ChannelEntity;
import com.acorn.finals.model.entity.ChannelMemberEntity;
import com.acorn.finals.model.entity.MemberEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface ChannelMemberMapper {

    List<ChannelMemberEntity> findAll();

    ChannelMemberEntity findOneById(int id);

    ChannelMemberEntity findOneByChannelIdAndMemberId(int channelId, int memberId);

    List<ChannelEntity> findAllChannelByMemberId(int memberId);

    List<MemberEntity> findAllMemberByChannelId(int channelId);

    int insert(ChannelMemberEntity entity);

    int update(ChannelMemberEntity entity);

    int deleteByChannelIdAndMemberId(int channelId, int memberId);
}

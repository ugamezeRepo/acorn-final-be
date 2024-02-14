package com.acorn.finals.mapper;

import com.acorn.finals.model.entity.ChannelEntity;
import com.acorn.finals.model.entity.ChannelMemberEntity;
import com.acorn.finals.model.entity.MemberEntity;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;


@Mapper
public interface ChannelMemberMapper {
    List<ChannelMemberEntity> findAll();

    ChannelMemberEntity findOneById(int id);

    List<ChannelEntity> findAllChannelByMemberId(int memberId);
    List<MemberEntity> findAllMemberByChannelId(int channelId);

    int insert(ChannelMemberEntity entity);

    int update(ChannelMemberEntity entity);

    int deleteById(int id);

}

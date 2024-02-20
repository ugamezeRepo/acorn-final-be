package com.acorn.finals.mapper;

import com.acorn.finals.model.entity.MemberConnectionEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MemberConnectionMapper {
    List<MemberConnectionEntity> findAllByChannelId(int channelId);

    int insert(MemberConnectionEntity entity);

    int update(MemberConnectionEntity entity);

    int deleteByNicknameAndHashtag(String nickname, int hashtag);
}

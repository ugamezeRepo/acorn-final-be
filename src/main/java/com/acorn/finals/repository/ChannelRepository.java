package com.acorn.finals.repository;

import com.acorn.finals.model.entity.ChannelEntity;
import com.acorn.finals.model.entity.ChannelMemberEntity;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ChannelRepository {
    private final SqlSession sqlSession;

    public List<ChannelEntity> selectAll() {
        return sqlSession.selectList("channel.selectAll");
    }

    public ChannelEntity selectOne(ChannelEntity channelEntity) {
        return sqlSession.selectOne("channel.selectOne", channelEntity);
    }

    public boolean insert(ChannelEntity channelEntity) {
        return sqlSession.insert("channel.insert", channelEntity) > 0;
    }

    public boolean update(ChannelEntity channelEntity) {
        return sqlSession.update("channel.update", channelEntity) > 0;
    }

    public boolean delete(ChannelEntity channelEntity) {
        return sqlSession.delete("channel.delete", channelEntity) > 0;
    }
}

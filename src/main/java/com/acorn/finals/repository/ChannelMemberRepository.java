package com.acorn.finals.repository;

import com.acorn.finals.model.entity.ChannelMemberEntity;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ChannelMemberRepository {
    private final SqlSession sqlSession;

    public List<ChannelMemberEntity> selectAll() {
        return sqlSession.selectList("channel_member.selectAll");
    }

    public ChannelMemberEntity selectOne(ChannelMemberEntity channelMemberEntity) {
        return sqlSession.selectOne("channel_member.selectOne", channelMemberEntity);
    }

    public int getNextSequence() {
        return sqlSession.selectOne("channel_member.getNextSequence");
    }

    public int insert(ChannelMemberEntity channelMemberEntity) {
        int id = getNextSequence();
        channelMemberEntity.setId(id);
        sqlSession.insert("channel_member.insert", channelMemberEntity);
        return id;
    }

    public boolean update(ChannelMemberEntity channelMemberEntity) {
        return sqlSession.update("channel_member.update", channelMemberEntity) > 0;
    }

    public boolean delete(ChannelMemberEntity channelMemberEntity) {
        return sqlSession.delete("channel_member.delete", channelMemberEntity) > 0;
    }
}

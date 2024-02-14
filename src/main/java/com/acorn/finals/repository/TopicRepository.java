package com.acorn.finals.repository;

import com.acorn.finals.model.entity.TopicEntity;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class TopicRepository {
    private final SqlSession sqlSession;

    public List<TopicEntity> selectAll() {
        return sqlSession.selectList("topic.selectAll");
    }

    public TopicEntity selectOne(TopicEntity topicEntity) {
        return sqlSession.selectOne("topic.selectOne", topicEntity);
    }

    public boolean insert(TopicEntity topicEntity) {
        return sqlSession.insert("topic.insert", topicEntity) > 0;
    }

    public boolean update(TopicEntity topicEntity) {
        return sqlSession.update("topic.update", topicEntity) > 0;
    }

    public boolean delete(TopicEntity topicEntity) {
        return sqlSession.delete("topic.delete", topicEntity) > 0;
    }
}

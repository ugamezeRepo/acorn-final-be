package com.acorn.finals.mapper;

import com.acorn.finals.model.entity.DirectMessageEntity;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DirectMessageMapper {

    List<DirectMessageEntity> findAllActiveByMemberId(int memberId);

    DirectMessageEntity findOneById(int id);

    DirectMessageEntity findOneByMemberIdAndAnotherId(int memberId, int anotherId);

    int insert(DirectMessageEntity entity);

    int activateDM(DirectMessageEntity entity);

    int deleteById(int id);

}

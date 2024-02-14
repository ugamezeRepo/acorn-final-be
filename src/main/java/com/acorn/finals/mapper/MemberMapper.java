package com.acorn.finals.mapper;

import com.acorn.finals.model.entity.MemberEntity;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface MemberMapper {
    List<MemberEntity> findAll();
    MemberEntity findOneById(int id);
    int insert(MemberEntity entity);
    int update(MemberEntity entity);
    int deleteById(int id);

}

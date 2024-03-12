package com.acorn.finals.mapper;

import com.acorn.finals.model.entity.RefreshTokenEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface RefreshTokenMapper {
    RefreshTokenEntity findOneTokenByMemberId(Integer memberId);

    RefreshTokenEntity findOneTokenByToken(String token);

    int insert(RefreshTokenEntity entity);

    int deleteByToken(String token);

    int update(RefreshTokenEntity entity);

    int deleteByMemberId(Integer memberId);
}

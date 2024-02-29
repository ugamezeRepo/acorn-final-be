package com.acorn.finals.mapper;

import com.acorn.finals.model.entity.RefreshTokenEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface RefreshTokenMapper {
    RefreshTokenEntity findOneTokenByEmail(String email);

    RefreshTokenEntity findOneTokenByToken(String token);

    int insert(RefreshTokenEntity entity);

    int deleteByToken(String token);

    int update(RefreshTokenEntity entity);

    int deleteByEmail(String email);
}

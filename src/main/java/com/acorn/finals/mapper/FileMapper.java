package com.acorn.finals.mapper;

import com.acorn.finals.model.entity.FileEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface FileMapper {
    FileEntity findOneById(int id);

    int insert(FileEntity entity);

    int deleteById(int id);

}

package com.acorn.finals.mapper;

import com.acorn.finals.model.entity.ImageEntity;

public interface ImageMapper {
    int insertImage(ImageEntity entity);

    ImageEntity contentCode(int id);
}

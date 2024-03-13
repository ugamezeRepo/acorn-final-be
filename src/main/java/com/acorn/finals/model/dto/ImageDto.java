package com.acorn.finals.model.dto;

import com.acorn.finals.model.entity.ImageEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ImageDto {
    private int id;
    private byte[] content;

    public ImageEntity toEntity() {
        return new ImageEntity(id, content);
    }
}

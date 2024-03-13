package com.acorn.finals.model.entity;

import com.acorn.finals.model.dto.ImageDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.ibatis.type.Alias;

@Alias("imageEntity")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ImageEntity {
    private int id;
    private byte[] content;

    public ImageDto toDto() {
        return new ImageDto(id, content);
    }
}

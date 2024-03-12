package com.acorn.finals.model.entity;

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
    private byte[] image;

}

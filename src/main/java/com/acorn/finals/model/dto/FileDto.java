package com.acorn.finals.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FileDto {
    String orgFileName;
    String saveFileName;
    Long fileSize;
    String filePath;
}

package com.acorn.finals.model.entity;

import com.acorn.finals.model.dto.FileDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.apache.ibatis.type.Alias;

@EqualsAndHashCode(callSuper = false)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Alias("fileEntity")
public class FileEntity extends BaseEntity {
    Integer id;
    String orgFileName;
    String saveFileName;
    long fileSize;
    String filePath;

    public FileDto toDto() { return new FileDto(orgFileName, saveFileName, fileSize, filePath); }
}

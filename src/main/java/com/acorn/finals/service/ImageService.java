package com.acorn.finals.service;

import com.acorn.finals.mapper.ImageMapper;
import com.acorn.finals.model.dto.ImageDto;
import com.acorn.finals.model.entity.ImageEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
@Slf4j
public class ImageService {
    private final ImageMapper mapper;

    public int insertImageByte(MultipartFile uploadFile) {
        ImageEntity imageEntity = new ImageEntity();
        try {
            byte[] content = uploadFile.getBytes();
            imageEntity.setContent(content);
            mapper.insertImage(imageEntity);
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);

        }


        return imageEntity.getId();
    }

    public ImageDto findImageByte(int id) {
        ImageEntity entity = mapper.contentCode(id);
        return entity.toDto();
    }

}

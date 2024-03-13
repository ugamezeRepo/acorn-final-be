package com.acorn.finals.controller;

import com.acorn.finals.model.dto.ImageDto;
import com.acorn.finals.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class ImageController {
    private final ImageService imageService;

    @PostMapping("/upload")
    public ResponseEntity<Integer> upload(@RequestParam("file") MultipartFile uploadFile) throws IOException {

        //파일명을 받는다 - 일반 원본파일명
        String filename = uploadFile.getOriginalFilename();
        //파일 확장자
        assert filename != null;
        String filename_ext = filename.substring(filename.lastIndexOf(".") + 1);
        //확장자를소문자로 변경
        filename_ext = filename_ext.toLowerCase();
        //이미지 검증 배열변수
        String[] imageFile = {"jpg", "png", "bmp", "gif"};
        //돌리면서 확장자가 이미지인지
        int cnt = 0;
        for (String s : imageFile) {
            if (filename_ext.equals(s)) {
                cnt++;
            }
        }
        //이미지가 아님
        if (cnt == 0) return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        int attachmentId = imageService.insertImageByte(uploadFile);

        return ResponseEntity
                .ok()
                .body(attachmentId);
    }

    @GetMapping("/attachment/{id}")
    public ResponseEntity<ImageDto> responseImageByte(@PathVariable("id") int id) {

        ImageDto responseDto = imageService.findImageByte(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(responseDto);
    }

}




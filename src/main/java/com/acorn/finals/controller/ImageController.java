package com.acorn.finals.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
public class ImageController {

    @PostMapping("/upload")
    public ResponseEntity<byte[]> upload(@RequestParam("file") MultipartFile uploadFile) throws IOException {


        //파일명을 받는다 - 일반 원본파일명
        String filename = uploadFile.getOriginalFilename();

        //파일 확장자
        String filename_ext = filename.substring(filename.lastIndexOf(".") + 1);
        //확장자를소문자로 변경
        filename_ext = filename_ext.toLowerCase();

        //이미지 검증 배열변수
        String[] imageFile = {"jpg", "png", "bmp", "gif"};

        //돌리면서 확장자가 이미지인지
        int cnt = 0;
        for (int i = 0; i < imageFile.length; i++) {
            if (filename_ext.equals(imageFile[i])) {
                cnt++;
            }
        }

        //이미지가 아님
        if (cnt == 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.ok().body(uploadFile.getBytes());
    }
    
}


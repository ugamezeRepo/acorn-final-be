package com.acorn.finals.service;

import com.acorn.finals.model.dto.FileDto;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Slf4j
public class FileService {
    @Value("${file.location}")
    private String fileLocation;

    public Map<String, Object> upload(MultipartFile myFile) {
        String orgFileName = myFile.getOriginalFilename();
        String saveFileName = UUID.randomUUID().toString();
        long fileSize = myFile.getSize();
        String filePath = fileLocation + File.separator + saveFileName;
        File dest = new File(filePath);
        try {
            myFile.transferTo(dest);
        } catch (Exception e) {
        }

        Map<String, Object> map = new HashMap<>();
        map.put("orgFileName", orgFileName);
        map.put("saveFileName", saveFileName);
        map.put("fileSize", fileSize);

        return map;
    }

    @GetMapping("/file/download")
    public ResponseEntity<InputStreamResource> download(FileDto dto) throws UnsupportedEncodingException, FileNotFoundException {
        String encodedName = URLEncoder.encode(dto.getOrgFileName(), "utf-8");
        encodedName = encodedName.replaceAll("\\+", " ");
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, "application/octet-stream");
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + encodedName);
        headers.setContentLength(dto.getFileSize());

        String filePath = fileLocation + File.separator + dto.getSaveFileName();
        InputStream is = new FileInputStream(filePath);
        InputStreamResource isr = new InputStreamResource(is);

        return ResponseEntity.ok().headers(headers).body(isr);
    }
}

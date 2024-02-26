package com.acorn.finals.controller;

import com.acorn.finals.annotation.WebSocketController;
import com.acorn.finals.annotation.WebSocketMapping;
import com.acorn.finals.service.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
@WebSocketController("/file")
@RequiredArgsConstructor
@Slf4j
public class FileController {
    private final FileService service;

    @WebSocketMapping
    public ResponseEntity<Void> uploadFile() {

        return ResponseEntity.ok().build();
    }
}

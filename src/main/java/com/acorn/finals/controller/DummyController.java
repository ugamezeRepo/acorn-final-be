package com.acorn.finals.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DummyController {
    @GetMapping("/dummy/deploy")
    public String dummy() {
        return "test";
    }

}

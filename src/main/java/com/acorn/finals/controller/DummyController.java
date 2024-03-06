package com.acorn.finals.controller;

import com.acorn.finals.model.Role;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DummyController {
    @GetMapping("/dummy/deploy")
    public String dummy() {
//        ROLE_GUEST("guest")
        // "guest" == E.ROLE_GUEST.toString();
        Role a = Role.valueOf("guest");



        return "test";
    }

}

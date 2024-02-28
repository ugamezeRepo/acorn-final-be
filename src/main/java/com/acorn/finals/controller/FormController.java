package com.acorn.finals.controller;

import com.acorn.finals.model.dto.ChannelDto;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class FormController {

    @PostMapping("/acceptInvite")
    public RedirectView responseUrl(@RequestBody ChannelDto dto) {

        return new RedirectView("https://dotori.site/channel/" + dto.getId());
    }
}

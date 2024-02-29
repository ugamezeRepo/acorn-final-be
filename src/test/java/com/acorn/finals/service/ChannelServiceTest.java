package com.acorn.finals.service;

import com.acorn.finals.model.dto.ChannelDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;

import java.util.List;

@SpringBootTest
class ChannelServiceTest {

    @Autowired
    private ChannelService channelService;

    @Test
    void createNewChannel_Test() {
        ChannelDto createNewChannelRequest = new ChannelDto();
        createNewChannelRequest.setName("새로운 테스트방");
        User user = new User("sonsanghee3@gmail.com", "", List.of());
        Authentication auth = new UsernamePasswordAuthenticationToken(user, List.of());
        channelService.createNewChannel(createNewChannelRequest, auth);
      
    }

}
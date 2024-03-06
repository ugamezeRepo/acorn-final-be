package com.acorn.finals.controller;

import com.acorn.finals.mapper.TopicMapper;
import com.acorn.finals.model.dto.TopicDto;
import com.acorn.finals.model.entity.TopicEntity;
import com.acorn.finals.service.ChannelService;
import com.acorn.finals.service.MemberService;
import com.acorn.finals.service.MessageService;
import com.acorn.finals.service.PersonalTopicService;
import com.acorn.finals.service.TopicService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
class ChannelControllerTest {

    @MockBean
    MemberService memberService;
    @MockBean
    TopicService topicService;
    @MockBean
    MessageService messageService;
    @MockBean
    ChannelService channelService;
    @MockBean
    PersonalTopicService personalTopicService;
    @MockBean
    TopicMapper topicMapper;

//    @Test
//    @DisplayName("Mock Bean 테스트")
//    void mockBeanHello() {
//        Assertions.assertNull(mockBean.hello());
//    }
    @Test
    void testCreateNewTopicWithValidRole() {
        // Mocking necessary dependencies
        // Mocking authentication object
        Authentication auth = new UsernamePasswordAuthenticationToken("test@example.com", null);
        int channelId = 1;
        TopicDto topicCreateRequest = new TopicDto(channelId,"Test Topic");
        // Setting up mock behavior
        when(memberService.getMemberChannelRole(anyString(), anyInt())).thenReturn("owner");
        when(topicMapper.insert(any())).thenReturn(1);

        // Creating instance of controller
        ChannelController controller = new ChannelController(channelService, topicService, messageService,memberService, personalTopicService);
        TopicEntity topicEntity = new TopicEntity();
        topicEntity.setId(1);
        // Performing test
        ResponseEntity<TopicDto> response = controller.createNewTopic(123, topicCreateRequest, auth);

        // Verifying the result
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(topicCreateRequest);


    }

    @Test
    void testCreateNewTopicWithInvalidRole() {
        // Mocking necessary dependencies
        MemberService memberService = mock(MemberService.class);
        TopicService topicService = mock(TopicService.class);

        // Mocking authentication object
        Authentication auth = new UsernamePasswordAuthenticationToken("test@example.com", null);

        // Setting up mock behavior
        when(memberService.getMemberChannelRole(anyString(), anyInt())).thenReturn("user");

        // Creating instance of controller
        ChannelController controller = new ChannelController(channelService, topicService, messageService, memberService, personalTopicService);

        // Performing test
        ResponseEntity<TopicDto> response = controller.createNewTopic(123, new TopicDto(), auth);

        // Verifying the result
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }
}



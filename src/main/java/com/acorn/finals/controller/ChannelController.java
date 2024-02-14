package com.acorn.finals.controller;

import com.acorn.finals.model.UrlResponse;
import com.acorn.finals.model.dto.ChannelDto;
import com.acorn.finals.model.dto.MemberDto;
import com.acorn.finals.model.dto.MessageDto;
import com.acorn.finals.model.dto.TopicDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/channel")
public class ChannelController {
    /**
     * create a new channel
     * @param channelCreateRequest channel create request with channel name
     * @return created channel
     */
    @PostMapping("/")
    public UrlResponse<ChannelDto> createNewChannel(ChannelDto channelCreateRequest) {
        return new UrlResponse<>("/channel/dummyNewChannelId", channelCreateRequest);
    }

    /**
     * update channel
     * @param channelUpdateRequest channel update request with new channel name, or thumbnail url
     * @return updated channel
     */
    @PatchMapping("/{channelId}")
    public ChannelDto updateChannel(ChannelDto channelUpdateRequest) {
        return channelUpdateRequest;
    }

    /**
     * list all members of channel
     * @param channelId id of the channel
     * @return list of members of the channel
     */
    @GetMapping("/{channelId}/member")
    public List<MemberDto> listAllMembers(@PathVariable String channelId) {
        return List.of(
            new MemberDto("dummy1@mail.com", "user1", 7777),
            new MemberDto("dummy2@mail.com", "user2", 7778),
            new MemberDto("dummy3@mail.com", "user3", 7779),
            new MemberDto("dummy4@mail.com", "user4", 7780),
            new MemberDto("dummy5@mail.com", "user5", 7781)
        );
    }

    /**
     * list all topics of channel
     * @param channelId id of the channel
     * @return list of topics of channel
     */
    @GetMapping("/{channelId}/topic")
    public List<UrlResponse<TopicDto>> listAllTopics(@PathVariable String channelId) {
        return List.of(
                new UrlResponse<>("/" + channelId + "/topic/dummyTopicId1", new TopicDto("topic1")),
                new UrlResponse<>("/" + channelId + "/topic/dummyTopicId2", new TopicDto("topic2")),
                new UrlResponse<>("/" + channelId + "/topic/dummyTopicId3", new TopicDto("topic3")),
                new UrlResponse<>("/" + channelId + "/topic/dummyTopicId4", new TopicDto("topic4")),
                new UrlResponse<>("/" + channelId + "/topic/dummyTopicId5", new TopicDto("topic5")),
                new UrlResponse<>("/" + channelId + "/topic/dummyTopicId6", new TopicDto("topic6"))
        );
    }

    /**
     * create new topic
     * @param channelId id of the channel that references topic
     * @param topicCreateRequest topic create request with title
     * @return created topic with title and url
     */
    @PostMapping("/{channelId}/topic")
    public UrlResponse<TopicDto> createNewTopic(@PathVariable String channelId, @RequestBody TopicDto topicCreateRequest) {
        return new UrlResponse<>("/" + channelId + "/dummyTopicId", topicCreateRequest);
    }


    /**
     * remove topic
     * @param channelId id of the channel that references topic
     * @param topicDeleteRequest topic delete request with url
     * @return HTTP STATUS 200 on success
     */
    @DeleteMapping("/{channelId}/topic")
    public ResponseEntity<Void> removeTopic(@PathVariable String channelId, @RequestBody TopicDto topicDeleteRequest) {
        return ResponseEntity.ok().build();
    }

    /**
     * update topic
     * @param channelId id of the channel that references topic
     * @param topicId id of the topic that will be updated
     * @param topicUpdateRequest topic update request with existing url and new title
     * @return update topic
     */
    @PatchMapping("/{channelId}/topic/{topicId}")
    public TopicDto updateTopic(@PathVariable String channelId, @PathVariable String topicId, @RequestBody TopicDto topicUpdateRequest) {
        return topicUpdateRequest;
    }

    /**
     * list all messages of the topic
     *
     * @param channelId id of the channel that references topic
     * @param topicId id of the topic that references message
     * @return list of the messages of the topic
     */
    @GetMapping("/{channelId}/topic/{topicId}/message")
    public List<MessageDto> listAllMessages(@PathVariable String channelId, @PathVariable String topicId) {
        return List.of(
                new MessageDto(new MemberDto("dummy@email.com", "user", 8888), "hello world", LocalDateTime.now()),
                new MessageDto(new MemberDto("dummy@email.com", "user", 8888), "hello world", LocalDateTime.now()),
                new MessageDto(new MemberDto("dummy@email.com", "user", 8888), "hello world", LocalDateTime.now()),
                new MessageDto(new MemberDto("dummy@email.com", "user", 8888), "hello world", LocalDateTime.now())
        );
    }

    /**
     * delete message
     * @param channelId id of the channel that references topic
     * @param topicId id of the topic that references message
     * @param messageId id of the message that will be deleted
     * @return HTTP STATUS 200 on success
     */
    @DeleteMapping("/{channelId}/topic/{topicId}/message/{messageId}")
    public ResponseEntity<Void> deleteMessage(@PathVariable String channelId, @PathVariable String topicId, @PathVariable String messageId) {
        return ResponseEntity.ok().build();
    }

    /**
     * update message
     * @param channelId id of the channel that references topic
     * @param topicId id of the channel that references message
     * @param messageId id of the message will be updated
     * @param updateMessageRequest update message request with new content
     * @return updated message
     */
    @PatchMapping("/{channelId}/topic/{topicId}/message/{messageId}")
    public MessageDto updateMessage(@PathVariable String channelId, @PathVariable String topicId, @PathVariable String messageId, MessageDto updateMessageRequest) {
        return updateMessageRequest;
    }
}

package com.acorn.finals.controller;

import com.acorn.finals.model.UrlResponse;
import com.acorn.finals.model.dto.ChannelDto;
import com.acorn.finals.model.dto.MemberDto;
import com.acorn.finals.model.dto.MessageDto;
import com.acorn.finals.model.dto.TopicDto;
import com.acorn.finals.service.ChannelService;
import com.acorn.finals.service.MessageService;
import com.acorn.finals.service.TopicService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/channel")
@RequiredArgsConstructor
public class ChannelController {
    private final ChannelService channelService;
    private final TopicService topicService;
    private final MessageService messageService;

    /**
     * find list all channels
     *
     * @return list of the channels
     */
    @GetMapping
    public List<ChannelDto> listAllChannels() {
        return channelService.listAllChannels();
    }

    /**
     * find a channel by id
     *
     * @param channelId id of the channel
     * @return found channel
     */
    @GetMapping("/{channelId}")
    public ChannelDto findChannelById(@PathVariable int channelId) {
        return channelService.findChannelById(channelId);
    }

    /**
     * create a new channel
     *
     * @param channelCreateRequest channel create request with channel name
     * @return created channel
     */
    @PostMapping
    public UrlResponse<ChannelDto> createNewChannel(@RequestBody ChannelDto channelCreateRequest) {
        return channelService.createNewChannel(channelCreateRequest);
    }

    /**
     * update channel
     *
     * @param channelUpdateRequest channel update request with new channel name, or thumbnail url
     * @return updated channel
     */
    @PatchMapping("/{channelId}")
    public ResponseEntity<ChannelDto> updateChannel(@PathVariable int channelId, @RequestBody ChannelDto channelUpdateRequest) {
        return ResponseEntity.ok(channelService.updateChannel(channelId, channelUpdateRequest));
    }

    /**
     * delete channel
     *
     * @param channelId id of the channel
     * @return HTTP STATUS 200 on success
     */
    @DeleteMapping("/{channelId}")
    public ResponseEntity<Void> deleteChannel(@PathVariable int channelId) {
        if (!channelService.deleteChannel(channelId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok().build();
    }
    /**
     * list all members of channel
     *
     * @param channelId id of the channel
     * @return list of members of the channel
     */
    @GetMapping("/{channelId}/member")
    public List<MemberDto> listAllMembers(@PathVariable int channelId) {
        return channelService.listChannelMembers(channelId);
    }

    /**
     * list all topics of channel
     *
     * @param channelId id of the channel
     * @return list of topics of channel
     */
    @GetMapping("/{channelId}/topic")
    public List<UrlResponse<TopicDto>> listAllTopics(@PathVariable int channelId) {
        return topicService.findAllByChannelId(channelId);
    }

    /**
     * create new topic
     *
     * @param channelId          id of the channel that references topic
     * @param topicCreateRequest topic create request with title
     * @return created topic with title and url
     */
    @PostMapping("/{channelId}/topic")
    public UrlResponse<TopicDto> createNewTopic(@PathVariable int channelId, @RequestBody TopicDto topicCreateRequest) {
        return topicService.createNewTopic(channelId, topicCreateRequest);
    }


    /**
     * remove topic
     *
     * @param channelId          id of the channel that references topic
     * @param topicDeleteRequest topic delete request with url
     * @return HTTP STATUS 200 on success
     */
    @DeleteMapping("/{channelId}/topic/{topicId}")
    public ResponseEntity<Void> removeTopic(
            @PathVariable int channelId,
            @PathVariable int topicId,
            @RequestBody TopicDto topicDeleteRequest) {
        if (!topicService.removeTopic(topicId, topicDeleteRequest)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(null);
    }

    /**
     * update topic
     *
     * @param channelId          id of the channel that references topic
     * @param topicId            id of the topic that will be updated
     * @param topicUpdateRequest topic update request with existing url and new title
     * @return on success, updated topic data with HTTP STATUS 200
     */
    @PatchMapping("/{channelId}/topic/{topicId}")
    public ResponseEntity<TopicDto> updateTopic(
            @PathVariable int channelId,
            @PathVariable int topicId,
            @RequestBody TopicDto topicUpdateRequest) {
        if (!topicService.updateTopic(channelId, topicId, topicUpdateRequest)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(topicUpdateRequest);
    }

    /**
     * list all messages of the topic
     *
     * @param channelId id of the channel that references topic
     * @param topicId   id of the topic that references message
     * @return list of the messages of the topic
     */
    @GetMapping("/{channelId}/topic/{topicId}/message")
    public List<MessageDto> listAllMessages(@PathVariable int channelId, @PathVariable int topicId) {
        return messageService.findAllByChannelIdAndTopicId(channelId, topicId);
    }

    /**
     * delete message
     *
     * @param channelId id of the channel that references topic
     * @param topicId   id of the topic that references message
     * @param messageId id of the message that will be deleted
     * @return HTTP STATUS 200 on success
     */
    @DeleteMapping("/{channelId}/topic/{topicId}/message/{messageId}")
    public ResponseEntity<Void> deleteMessage(@PathVariable String channelId, @PathVariable String topicId, @PathVariable String messageId) {
        return ResponseEntity.ok().build();
    }

    /**
     * update message
     *
     * @param channelId            id of the channel that references topic
     * @param topicId              id of the channel that references message
     * @param messageId            id of the message will be updated
     * @param updateMessageRequest update message request with new content
     * @return updated message
     */
    @PatchMapping("/{channelId}/topic/{topicId}/message/{messageId}")
    public MessageDto updateMessage(@PathVariable String channelId, @PathVariable String topicId, @PathVariable String messageId, MessageDto updateMessageRequest) {
        return updateMessageRequest;
    }
}

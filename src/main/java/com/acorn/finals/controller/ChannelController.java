package com.acorn.finals.controller;

import com.acorn.finals.model.dto.*;
import com.acorn.finals.service.ChannelService;
import com.acorn.finals.service.MemberService;
import com.acorn.finals.service.MessageChannelService;
import com.acorn.finals.service.TopicService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/channel")
@RequiredArgsConstructor
public class ChannelController {
    private final ChannelService channelService;
    private final TopicService topicService;
    private final MessageChannelService messageChannelService;
    private final MemberService memberService;

    /**
     * 모든 채널의 정보를 가져옵니다
     *
     * @return list of the channels
     */
    @GetMapping
    public List<ChannelDto> listAllChannels() {
        return channelService.listAllChannels();
    }

    /**
     * 채널 초대 코드에 해당하는 채널의 정보를 가져옵니다
     *
     * @param inviteCode
     * @return ChannelDto return
     */
    @PostMapping("/invite/{code}")
    public ChannelDto responseChannelInfo(@PathVariable("code") String inviteCode) {
        return channelService.findChannelInfoByInviteCode(inviteCode);
    }

    /**
     * 채널 id 를 통해 채널의 정보를 가져옵니다
     *
     * @param channelId id of the channel
     * @return found channel
     */
    @GetMapping("/{channelId}")
    public ChannelDto findChannelById(@PathVariable int channelId) {
        return channelService.findChannelById(channelId);
    }

    /**
     * 새로운 채널을 생성합니다
     *
     * @param channelCreateRequest channel create request with channel name
     * @return created channel
     */
    @PostMapping
    public ChannelDto createNewChannel(@RequestBody ChannelDto channelCreateRequest, Authentication authentication) {
        return channelService.createNewChannel(channelCreateRequest, authentication);
    }

    /**
     * 채널에 들어갑니다
     *
     * @param code(inviteCode)
     * @return ResponserEntity<ChannelDto>
     */
    @PostMapping("/join/{code}")
    public ResponseEntity<ChannelDto> joinChannel(@PathVariable("code") String code, Authentication auth) {
        var memberId = Integer.parseInt(auth.getName());
        var channelInfo = channelService.joinMember(memberId, code, "guest");
        return ResponseEntity.ok(channelInfo);
    }

    /**
     * 채널에서의 권한을 변경합니다. 단 채널 관리 권한이 존재해야합니다.
     *
     * @param dto
     * @param authentication
     * @param channelId
     * @return
     */
    @PatchMapping("{channelId}/role")
    public boolean changeRole(@RequestBody ChangeRoleRequestDto dto, Authentication authentication, @PathVariable("channelId") int channelId) {
        var ownerId = Integer.parseInt(authentication.getName());
        dto.setOwnerId(ownerId);
        dto.setChannelId(channelId);
        return channelService.changeRole(dto);
    }

    /**
     * 채널의 정보를 수정합니다. 단 채널 관리 권한이 존재해야합니다
     *
     * @param channelUpdateRequest channel update request with new channel name, or thumbnail url
     * @return updated channel
     */
    @PatchMapping("/{channelId}")
    public ResponseEntity<ChannelDto> updateChannel(@PathVariable int channelId, @RequestBody ChannelDto channelUpdateRequest) {
        // TODO: 권한 검증
        return ResponseEntity.ok(channelService.updateChannel(channelId, channelUpdateRequest));
    }

    /**
     * 채널을 삭제합니다. 단 채널 관리 권한이 존재해야합니다
     *
     * @param channelId id of the channel
     * @return HTTP STATUS 200 on success
     */
    @DeleteMapping("/{channelId}")
    public ResponseEntity<Void> deleteChannel(@PathVariable int channelId, Authentication auth) {
        var memberId = Integer.parseInt(auth.getName());
        var role = memberService.getMemberChannelRole(memberId, channelId);
        if (!"owner".equals(role)) {
            return ResponseEntity.badRequest().build();
        }
        if (!channelService.deleteChannel(channelId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok().build();
    }


    /**
     * 채널에 있는 모든 멤버 목록을 불러옵니다. 단 채널에 참여중이여야합니다
     *
     * @param channelId id of the channel
     * @return list of members of the channel
     */
    @GetMapping("/{channelId}/member")
    public List<MemberDto> listAllMembers(@PathVariable int channelId) {
        // TODO: 채널 참여중 체크
        return channelService.listChannelMembers(channelId);
    }


    /**
     * 채널에 있는 모든 토픽 목록을 불러옵니다. 단 채널에 참여 중이여야합니다.
     *
     * @param channelId id of the channel
     * @return list of topics of channel
     */
    @GetMapping("/{channelId}/topic")
    public List<TopicDto> listAllTopics(@PathVariable int channelId) {
        // TODO: 채널 참여중 체크 
        return topicService.findAllByChannelId(channelId);
    }

    /**
     * 새로운 토픽을 생성합니다. 단 채널의 토픽 생성 권한이 있어야 합니다.
     *
     * @param channelId          id of the channel that references topic
     * @param topicCreateRequest topic create request with title
     * @return created topic with title and url
     */
    @PostMapping("/{channelId}/topic")
    public ResponseEntity<TopicDto> createNewTopic(@PathVariable int channelId, @RequestBody TopicDto topicCreateRequest, Authentication auth) {
        var memberId = Integer.parseInt(auth.getName());
        var role = memberService.getMemberChannelRole(memberId, channelId);
        if (!"owner".equals(role) && !"manager".equals(role)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        TopicDto newTopicDto = topicService.createNewTopic(channelId, topicCreateRequest);
        return ResponseEntity.ok().body(newTopicDto);
    }

    /**
     * 토픽을 삭제합니다. 단 채널의 토픽 삭제 권한이 있어야합니다
     *
     * @param channelId id of channel that refrences topic
     * @param topicId   id of topic
     * @param auth
     * @return
     */
    @DeleteMapping("/{channelId}/topic/{topicId}")
    public ResponseEntity<Void> removeTopic(@PathVariable int channelId, @PathVariable int topicId, Authentication auth) {
        var memberId = Integer.parseInt(auth.getName());
        var role = memberService.getMemberChannelRole(memberId, channelId);
        if (!"owner".equals(role) && !"manager".equals(role)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        if (!topicService.removeTopic(channelId, topicId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok().build();
    }

    /**
     * 토픽 정보를 수정합니다. 단 채널의 토픽 수정 권한이 있어야 합니다.
     *
     * @param channelId          id of the channel that references topic
     * @param topicId            id of the topic that will be updated
     * @param topicUpdateRequest topic update request with existing url and new title
     * @return on success, updated topic data with HTTP STATUS 200
     */
    @PatchMapping("/{channelId}/topic/{topicId}")
    public ResponseEntity<TopicDto> updateTopic(@PathVariable int channelId, @PathVariable int topicId, @RequestBody TopicDto topicUpdateRequest, Authentication auth) {
        var memberId = Integer.parseInt(auth.getName());
        String role = memberService.getMemberChannelRole(memberId, channelId);

        if (!role.equals("owner") && !role.equals("manager")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        if (!topicService.updateTopic(channelId, topicId, topicUpdateRequest)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(topicUpdateRequest);
    }

    /**
     * 채널의 토픽에 있는 모든 메시지들을  불러옵니다. 단 채널에 참여 중이여야 합니다.
     *
     * @param channelId id of the channel that references topic
     * @param topicId   id of the topic that references message
     * @return list of the messages of the topic
     */
    @GetMapping("/{channelId}/topic/{topicId}/message")
    public List<MessageDto> listAllMessages(@PathVariable int channelId, @PathVariable int topicId) {
        return messageChannelService.findAllByChannelIdAndTopicId(channelId, topicId);
    }

}

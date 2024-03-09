package com.acorn.finals.controller;

import com.acorn.finals.model.dto.DirectMessageDto;
import com.acorn.finals.service.DirectMessageService;
import com.acorn.finals.service.MemberService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/direct-messge")
@RequiredArgsConstructor
public class DirectMessageController {
    private final DirectMessageService directMessageService;
    private final MemberService memberService;

    /**
     * DM토픽 목록을 불러옵니다.
     *
     * @return list of DM topic
     */
    @GetMapping
    public List<DirectMessageDto> listAllActiveDM(Authentication auth) {
        return directMessageService.findAllActiveDM(auth.getName());
    }

    /**
     * url을 통해 DM토픽 정보를 불러옵니다.
     *
     * @param id id of the direct message topic
     * @return DM topic
     */
    @GetMapping("/{id}")
    public DirectMessageDto findDMById(@PathVariable int id) {
        return directMessageService.findOneById(id);
    }

    /**
     * 새로운 DM 을 만듭니다.
     *
     * @param directMessageCreateRequest topic create request with another's id
     * @return created topic
     */
    @PostMapping
    public ResponseEntity<DirectMessageDto> createNewPersonalTopic(@RequestBody DirectMessageDto directMessageCreateRequest, Authentication auth) {
        directMessageService.createNewDM(directMessageCreateRequest, auth);

        return null;
    }

    /**
     *  DM 을 삭제합니다.
     *
     * @param id id of the personal topic
     * @return HTTP STATUS 200 on success
     */
    @PatchMapping("/@me/{id}")
    public ResponseEntity<Void> deactiveDM(@PathVariable int id) {
        directMessageService.deactivateDM(id);

        return ResponseEntity.ok(null);
    }
}

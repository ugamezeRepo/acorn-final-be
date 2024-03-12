package com.acorn.finals.controller;

import ch.qos.logback.core.status.Status;
import com.acorn.finals.mapper.MemberMapper;
import com.acorn.finals.model.dto.DirectMessageDto;
import com.acorn.finals.service.DirectMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/direct-message")
@RequiredArgsConstructor
public class DirectMessageController {
    private final DirectMessageService directMessageService;
    private final MemberMapper memberMapper;

    /**
     * DM토픽 목록을 불러옵니다.
     *
     * @return list of DM topic
     */
    @GetMapping
    public List<DirectMessageDto> listAllActiveDM(Authentication auth) {
        var memberId = Integer.parseInt(auth.getName());
        return directMessageService.findAllActiveDM(memberId);
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
     * 새로운 DM을 만듭니다.
     *
     * @param directMessageCreateRequest topic create request with another's id
     * @return created topic
     */
    @PostMapping
    public ResponseEntity<DirectMessageDto> createNewPersonalTopic(@RequestBody DirectMessageDto directMessageCreateRequest, Authentication auth) {
        DirectMessageDto createdDM = directMessageService.createNewDM(directMessageCreateRequest, auth);

        return ResponseEntity.status(HttpStatus.OK).body(createdDM);
    }

    /**
     * DM을 목록에서 보이지 않도록 비활성화합니다.
     *
     * @param id id of the direct message topic
     * @return HTTP STATUS 200 on success
     */
    @PutMapping("/{id}")
    public ResponseEntity<DirectMessageDto> activateDM(@PathVariable int id,
                                                       @RequestBody DirectMessageDto directMessageDMActivateRequest, Authentication auth) {
        int memberId = Integer.parseInt(auth.getName());
        var dm = directMessageService.findOneById(id);
        if (memberId != dm.getMemberId()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        DirectMessageDto updatedDM = directMessageService.activateDM(id, directMessageDMActivateRequest);

        return ResponseEntity.status(HttpStatus.OK).body(updatedDM);
    }

    /**
     * DM을 삭제합니다.
     *
     * @param id id of the direct message topic
     * @return HTTP STATUS 200 on success
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDM(@PathVariable int id, Authentication auth) {
        int memberId = Integer.parseInt(auth.getName());
        if (memberId != directMessageService.findOneById(id).getMemberId()) {
            return ResponseEntity.status(Status.WARN).build();
        }

        if (directMessageService.deleteDM(id)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        return ResponseEntity.ok().build();
    }
}

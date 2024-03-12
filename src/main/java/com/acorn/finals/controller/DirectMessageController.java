package com.acorn.finals.controller;

import ch.qos.logback.core.status.Status;
import com.acorn.finals.model.dto.DirectMessageDto;
import com.acorn.finals.service.DirectMessageService;
import com.acorn.finals.service.MemberService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/direct-message")
@RequiredArgsConstructor
@Slf4j
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
     * 새로운 DM을 만듭니다.
     *
     * @param directMessageCreateRequest topic create request with another's id
     * @return created topic
     */
    @PostMapping
    public ResponseEntity<DirectMessageDto> createNewPersonalTopic(@RequestBody DirectMessageDto directMessageCreateRequest, Authentication auth) {
        int memberId = memberService.findMemberByEmail(auth.getName()).getId();
        int anotherId = directMessageCreateRequest.getAnotherId();

        try {
            DirectMessageDto isExistedDM = directMessageService.findOneByMemberIdAndAnotherId(memberId, anotherId);
            if (isExistedDM != null) {
                isExistedDM.setActive(1);
                directMessageService.activateDM(isExistedDM.getId(), isExistedDM);
                return ResponseEntity.status(HttpStatus.OK).body(isExistedDM);
            }
        } catch (RuntimeException e) {
            log.info("이미 존재하는 DM입니다.");
        }

        DirectMessageDto createdDM = directMessageService.createNewDM(directMessageCreateRequest, auth);
        return ResponseEntity.status(HttpStatus.OK).body(createdDM);
    }

    /**
     *  DM을 목록에서 보이지 않도록 비활성화합니다.
     *
     * @param id id of the direct message topic
     * @return HTTP STATUS 200 on success
     */
    @PutMapping("/{id}")
    public ResponseEntity<DirectMessageDto> activateDM(@PathVariable int id,
            @RequestBody DirectMessageDto directMessageDMActivateRequest, Authentication auth) {
        int memberId = memberService.findMemberByEmail(auth.getName()).getId();
        if (memberId != directMessageService.findOneById(id).getMemberId()) {
            return ResponseEntity.status(Status.WARN).build();
        }
        DirectMessageDto updatedDM = directMessageService.activateDM(id, directMessageDMActivateRequest);

        return ResponseEntity.status(HttpStatus.OK).body(updatedDM);
    }

    /**
     *  DM을 삭제합니다.
     *
     * @param id id of the direct message topic
     * @return HTTP STATUS 200 on success
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDM(@PathVariable int id, Authentication auth) {
        int memberId = memberService.findMemberByEmail(auth.getName()).getId();
        if (memberId != directMessageService.findOneById(id).getMemberId()) {
            return ResponseEntity.status(Status.WARN).build();
        }

        if (directMessageService.deleteDM(id)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        return ResponseEntity.ok().build();
    }
}

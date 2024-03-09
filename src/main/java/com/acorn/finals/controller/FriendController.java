package com.acorn.finals.controller;

import com.acorn.finals.model.dto.RequestFriendDto;
import com.acorn.finals.service.FriendService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/friend")
public class FriendController {
    private final FriendService friendService;

    @PostMapping("/request")
    public ResponseEntity<RequestFriendDto> requestFriend(@RequestBody RequestFriendDto requestDto) {

        RequestFriendDto responseDto = friendService.addFriendRequest(requestDto.toEntity());


        return ResponseEntity.status(HttpStatus.OK)
                .body(responseDto);
    }

    @GetMapping("/request")
    public ResponseEntity<List<RequestFriendDto>> requestFriendList(@RequestBody RequestFriendDto requestDto) {

        List<RequestFriendDto> responseDto = friendService.friendRequestList(requestDto);

        return ResponseEntity.status(HttpStatus.OK)
                .body(responseDto);
    }

    @DeleteMapping("/request")
    public ResponseEntity<Boolean> requestAnswerAndDelete(@RequestBody RequestFriendDto requestDto) {
        Boolean result = friendService.friendListAnswerAndDelete(requestDto);
        return ResponseEntity.status(HttpStatus.OK)
                .body(result);
    }

}

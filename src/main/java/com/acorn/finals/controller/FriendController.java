package com.acorn.finals.controller;

import com.acorn.finals.model.dto.MemberDto;
import com.acorn.finals.model.dto.RequestFriendDto;
import com.acorn.finals.service.FriendService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequiredArgsConstructor
@RequestMapping("/friend")
public class FriendController {
    private final FriendService friendService;

    /**
     * @param requestDto fromId , toId
     * @return  작업이 완료되면 true 를 return 이미 중복된 요청이거나 요청중 오류발생시 fasle return
     */
    @PostMapping("/request")
    public ResponseEntity<Boolean> requestFriend(@RequestBody RequestFriendDto requestDto) {

        Boolean isSuccess = friendService.addFriendRequest(requestDto.toEntity());


        return ResponseEntity.status(HttpStatus.OK)
                .body(isSuccess);
    }

    /**
     * @param requestDto fromId, toid
     * @return 내가 받은 친구요청의 회원 정보가 들어 있습니다
     */

    @GetMapping("/request")
    public ResponseEntity<List<MemberDto>> requestFriendList(@RequestBody RequestFriendDto requestDto) {

        List<MemberDto> responseDto = friendService.friendRequestList(requestDto);

        return ResponseEntity.status(HttpStatus.OK)
                .body(responseDto);
    }

    /**
     * @param requestDto answer 이란 값으로 yes or no 를 보내 주세요 fromId, toId
     * @return 요청에 대한 처리의 성공여부를 응답합니다
     */
    @DeleteMapping("/request")
    public ResponseEntity<Boolean> requestAnswerAndDelete(@RequestBody RequestFriendDto requestDto) {
        Boolean result = friendService.friendListAnswerAndDelete(requestDto);
        return ResponseEntity.status(HttpStatus.OK)
                .body(result);
    }

    /**
     * @param id
     * @return 자신과 친구인 전체 목록
     */
    @GetMapping("{id}/list")
    public ResponseEntity<List<MemberDto>> friendAllList(@PathVariable("id") int id) {
        List<MemberDto> responseDto = friendService.friendList(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(responseDto);
    }

    /**
     * @param my_id   지금 나의 id 를 필요로함
     * @param keyword url 로  get 방식으로 보내주길 바람 ex) friend/1/search?keyword= '검색값'
     * @return 자신의 친구를 제외한 유저 정보들이 들어있음
     */
    @GetMapping("{id}/search")
    public ResponseEntity<List<MemberDto>> searchFriend(@PathVariable("id") int my_id, @RequestParam("keyword") String keyword) {
        Map<String, Object> map = new HashMap<>();
        map.put("my_id", my_id);
        map.put("keyword", keyword);
        List<MemberDto> requestDto = friendService.findNewFriend(map);
        return ResponseEntity.status(HttpStatus.OK)
                .body(requestDto);

    }

}

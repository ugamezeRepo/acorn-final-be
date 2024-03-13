package com.acorn.finals.util;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class HangulUtilsTest {

    @Test
    void dissectHangul() {
        assertThat(HangulUtils.dissectHangul("한글")).isEqualTo("ㅎㅏㄴㄱㅡㄹ");
        assertThat(HangulUtils.dissectHangul("난a짱123")).isEqualTo("ㄴㅏㄴaㅉㅏㅇ123");
        assertThat(HangulUtils.dissectHangul("just english")).isEqualTo("just english");
        assertThat(HangulUtils.dissectHangul("왜 외부인은 출입금지인거죠?"))
                .isEqualTo("ㅇㅗㅐ ㅇㅗㅣㅂㅜㅇㅣㄴㅇㅡㄴ ㅊㅜㄹㅇㅣㅂㄱㅡㅁㅈㅣㅇㅣㄴㄱㅓㅈㅛ?");
        assertThat(HangulUtils.dissectHangul("😀😁😂🤣😃")).isEqualTo("😀😁😂🤣😃");
    }
}
package com.acorn.finals.util;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;


class PathUtilTest {

    @Test
    void extractPath() {

        var extractedMap = PathUtil.extractPath("/channel/{channelId}/topic/{topicId}", "/channel/1/topic/2");
        assertThat(extractedMap).isEqualTo(Map.ofEntries(
                Map.entry("channelId", "1"),
                Map.entry("topicId", "2")
        ));

        var extractedMap2 = PathUtil.extractPath("/channel/*/topic/{topicId}", "/channel/100/topic/200");
        assertThat(extractedMap2).isEqualTo(Map.ofEntries(
                Map.entry("topicId", "200")
        ));

    }
}
package com.acorn.finals;

import com.acorn.finals.annotation.WebSocketMapping;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.PathVariable;

@SpringBootApplication
public class FinalsApplication {
    public static void main(String[] args) {
        SpringApplication.run(FinalsApplication.class, args);
    }


    @WebSocketMapping("/channel/{channelId}/topic/{topicId}")
    public String something(@PathVariable int topicId, @PathVariable int channelId) {
        return null;
    }
}

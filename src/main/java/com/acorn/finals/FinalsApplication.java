package com.acorn.finals;

import com.acorn.finals.annotation.WebSocketMapping;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.server.RequestPath;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.util.pattern.PathPatternParser;

import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
public class FinalsApplication {
    public static void main(String[] args) {
        SpringApplication.run(FinalsApplication.class, args);
    }


    @WebSocketMapping("/channel/{channelId}/topic/{topicId}")
    public String something(@PathVariable int topicId, @PathVariable int channelId){
        return null;
    }

    private static void 추출하는법을알려주마() {
        PathPatternParser parser = new PathPatternParser();

//       session.getUri()



//      fullPath = basePath + additionalPath
        // 어노테이션 정보를 통해서 획득 가능
        String fullPath = "/channel/{abcd}/topic/{efgh}";





        // @Value()
        String contextPath = "/api" ;



        String fullPathWithContext = contextPath + fullPath;


        // WebsocketInfo   fullpath 관련 정보를 넣어주어야 함


        // Handler 안에서 처리
        // uri = ws://localhost:9000/api/channel/123/topic/34
        // uri 파싱하면 바로 나옴
        String rawPath = "/api/channel/1/topic/2";



        var orgResult = RequestPath.parse(fullPathWithContext, "");
        var result = RequestPath.parse(rawPath, "/api");



        // extracted pattern
        var pattern = parser.parse(fullPath);
        // channel, /, {channelId}, / , topic, /, {topicId}
        var orgPattern = parser.parse(fullPath);



        var extracted = pattern.extractPathWithinPattern(result);
        var org = orgPattern.extractPathWithinPattern(orgResult);
        // channel, /, 1, topic, /, 2


        var orgArgsList = org.elements();
        var extractedArgsList = extracted.elements();

        Map<String, String> kvPair = new HashMap<>();
        for (int i = 0 ; i < orgArgsList.size(); i++) {
            var key = orgArgsList.get(i).value();
            var value = extractedArgsList.get(i).value();

            if (key.equals(value) || key.equals("*")) continue;

            assert (key.charAt(0) == '{' && key.charAt(key.length() - 1) == '}');

            var extractedKeyName = key.substring(1, key.length() - 1);
            kvPair.put(extractedKeyName, value);
        }


        for (var entry : kvPair.entrySet()) {
            System.out.println(String.format("%s : %s", entry.getKey(), entry.getValue()));
        }
//        System.out.println(wtf.toString());
    }

    @Bean
    CommandLineRunner runner() {
        return arg -> {
            추출하는법을알려주마();
        };
    }
}

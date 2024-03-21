package com.acorn.finals.model.dto.websocket;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RtcSignalDto {
    private Map<String, Object> candidate;
    private Map<String, Object> desc;
    private String uuid;
}

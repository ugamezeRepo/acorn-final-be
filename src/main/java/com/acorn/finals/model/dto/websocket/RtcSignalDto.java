package com.acorn.finals.model.dto.websocket;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RtcSignalDto {
    private String type;
    private String uuid;
    private String target;
    private Map<String, Object> payload;
}

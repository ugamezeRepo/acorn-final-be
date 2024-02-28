package com.acorn.finals.model.dto.websocket;

import lombok.Data;

@Data
public class RtcSignalDto {
    private String type;
    private String dest;
    private String data;
}

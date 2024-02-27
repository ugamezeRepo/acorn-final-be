package com.acorn.finals.model.property;

import lombok.Data;

@Data
public class TokenProperty {
    String secret;
    Integer expiration;
}

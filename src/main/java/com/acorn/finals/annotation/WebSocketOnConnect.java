package com.acorn.finals.annotation;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface WebSocketOnConnect {
    String value() default "";
}

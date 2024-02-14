package com.acorn.finals.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan("com.acorn.finals.mapper")
public class MyBatisConfig {
}

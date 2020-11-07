package com.sanelee.calling;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.sanelee.calling.mapper")
public class CallingApplication {

    public static void main(String[] args) {
        SpringApplication.run(CallingApplication.class, args);
    }

}

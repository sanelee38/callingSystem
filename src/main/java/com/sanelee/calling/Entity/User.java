package com.sanelee.calling.Entity;


import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName(value = "user")
public class User {
    private int id;
    private String username;
    private int userAge;
    private String gender;
    private String phoneNumber;
    private int orderNumber;
    private String dateTime;
    private int score;
    private int flag;
}

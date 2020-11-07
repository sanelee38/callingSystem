package com.sanelee.calling.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sanelee.calling.Entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;


@Mapper
public interface UserMapper extends BaseMapper<User> {

    @Select("select * from user where phone_number=#{phone_number} order by date_time desc")
    List<User> findByPhone(String phone_number);

    @Select("select * from user where flag=#{flag} order by user.order_number")
    List<User> findByFlag(int flag);
}

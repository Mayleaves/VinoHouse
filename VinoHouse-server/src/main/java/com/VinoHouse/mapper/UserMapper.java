package com.VinoHouse.mapper;

import com.VinoHouse.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.Map;

@Mapper
public interface UserMapper {

    /**
     * 根据 openid 查询用户
     */
    @Select("select * from user where openid = #{openid}")
    User getByOpenid(String openid);

    /**
     * 插入数据
     */
    void insert(User user);

    @Select("select * from user where id = #{id}")
    User getById(Long userId);

    /**
     * 根据动态条件统计用户数量
     */
    Integer countByMap(Map map);
}

package com.miaosha.m.mapper;

import com.miaosha.m.entity.MiaoshaUser;
import org.apache.ibatis.annotations.*;

@Mapper
public interface MiaoshaUserMapper {

    // 1. 根据 ID（手机号）查询用户
    // 修改点：将参数名由 nickname 改为 id，SQL 语句明确匹配数据库的 id 列
    @Select("select * from miaosha_user where id = #{id}")
    public MiaoshaUser getById(@Param("id") String id);

    // 2. 更新用户信息（用于更新密码）
    // 修改点：根据 MiaoshaUser 对象的 id 属性进行更新
    @Update("update miaosha_user set password = #{password} where id = #{id}")
    public void update(MiaoshaUser toBeUpdate);

    // 3. 插入用户
    @Insert("insert into miaosha_user(id, password, salt, nickname, register_date) " +
            "values(#{id}, #{password}, #{salt}, #{nickname}, #{registerDate})")
    public void insertMiaoShaUser(MiaoshaUser user);
}

package com.miaosha.m.mapper;

import com.miaosha.m.entity.MiaoshaOrder;
import com.miaosha.m.entity.OrderInfo;
import org.apache.ibatis.annotations.*;

@Mapper
public interface OrderMapper {

    // 简历核心点：根据用户ID和商品ID查询，用于防止重复下单
    @Select("select * from miaosha_order where user_id=#{userId} and goods_id=#{goodsId}")
    MiaoshaOrder getMiaoshaOrderByUserIdGoodsId(@Param("userId") Long userId, @Param("goodsId") long goodsId);

    // 插入普通订单并返回主键ID
    @Insert("insert into order_info(user_id, goods_id, goods_name, goods_count, goods_price, status, create_date) " +
            "values(#{userId}, #{goodsId}, #{goodsName}, #{goodsCount}, #{goodsPrice}, #{status}, #{createDate})")
    @SelectKey(keyColumn="id", keyProperty="id", resultType=long.class, before=false, statement="select last_insert_id()")
    long insert(OrderInfo orderInfo);

    // 插入秒杀订单记录
    @Insert("insert into miaosha_order (user_id, goods_id, order_id) values(#{userId}, #{goodsId}, #{orderId})")
    int insertMiaoshaOrder(MiaoshaOrder miaoshaOrder);
}

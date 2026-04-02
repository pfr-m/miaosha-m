package com.miaosha.m.service;

import com.miaosha.m.entity.MiaoshaUser;
import com.miaosha.m.entity.OrderInfo;
import com.miaosha.m.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MiaoshaService {

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private OrderService orderService;

    /**
     * 核心秒杀事务：减库存 -> 下订单
     */
    @Transactional
    public OrderInfo miaosha(MiaoshaUser user, GoodsVo goods) {
        // 1. 减库存：执行 SQL update stock = stock - 1 where stock > 0
        boolean success = goodsService.reduceStock(goods);
        if (success) {
            // 2. 下订单：写入 order_info 和 miaosha_order 表
            return orderService.createOrder(user, goods);
        } else {
            // 3. 数据库库存不足
            return null;
        }
    }
}

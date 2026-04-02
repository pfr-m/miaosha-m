package com.miaosha.m.service;

import com.miaosha.m.entity.MiaoshaOrder;
import com.miaosha.m.entity.MiaoshaUser;
import com.miaosha.m.entity.OrderInfo;
import com.miaosha.m.mapper.OrderMapper;
import com.miaosha.m.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Date;

@Service
public class OrderService {

    @Autowired
    private OrderMapper orderMapper;

    // 供 Controller 调用，判断是否重复秒杀
    public MiaoshaOrder getMiaoshaOrderByUserIdGoodsId(Long userId, long goodsId) {
        return orderMapper.getMiaoshaOrderByUserIdGoodsId(userId, goodsId);
    }

    // 核心订单创建逻辑，由 MiaoshaService 调用
    @Transactional
    public OrderInfo createOrder(MiaoshaUser user, GoodsVo goods) {
        // 1. 生成普通订单
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setCreateDate(new Date());
        orderInfo.setGoodsCount(1);
        orderInfo.setGoodsId(goods.getId());
        orderInfo.setGoodsName(goods.getGoodsName());
        orderInfo.setGoodsPrice(goods.getMiaoshaPrice());
        orderInfo.setUserId(user.getId());
        orderInfo.setStatus(0); // 0-新建未支付
        orderMapper.insert(orderInfo);

        // 2. 生成秒杀订单记录
        MiaoshaOrder miaoshaOrder = new MiaoshaOrder();
        miaoshaOrder.setGoodsId(goods.getId());
        miaoshaOrder.setOrderId(orderInfo.getId());
        miaoshaOrder.setUserId(user.getId());
        orderMapper.insertMiaoshaOrder(miaoshaOrder);

        return orderInfo;
    }
}

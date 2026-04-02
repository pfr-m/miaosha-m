package com.miaosha.m.rabbitmq;

import com.miaosha.m.redis.RedisService;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MQSender {

    @Autowired
    AmqpTemplate amqpTemplate;

    @Autowired
    RedisService redisService; // 1. 注入 RedisService 实例

    public void sendMiaoshaMessage(MiaoshaMessage mm) {
        // 2. 使用对象名 redisService 调用方法
        String msg = redisService.beanToString(mm);
        amqpTemplate.convertAndSend(MQConfig.MIAOSHA_QUEUE, msg);
    }
}

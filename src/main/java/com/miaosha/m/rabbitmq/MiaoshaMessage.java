package com.miaosha.m.rabbitmq;

import com.miaosha.m.entity.MiaoshaUser;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MiaoshaMessage {
    private MiaoshaUser user;
    private long goodsId;
}

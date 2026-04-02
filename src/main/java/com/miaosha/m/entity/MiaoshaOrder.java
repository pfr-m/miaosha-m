package com.miaosha.m.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MiaoshaOrder {
    private Long id;
    private Long userId;
    private Long orderId;
    private Long goodsId;
}

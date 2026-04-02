package com.miaosha.m.entity;

import lombok.Getter;
import lombok.Setter;
import java.util.Date;

@Getter
@Setter
public class OrderInfo {
    private Long id;
    private Long userId;
    private Long goodsId;
    private String goodsName;
    private Integer goodsCount;
    private Double goodsPrice;
    private Integer status;
    private Date createDate;
    private Date payDate;
}

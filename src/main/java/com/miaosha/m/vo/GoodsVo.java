package com.miaosha.m.vo;

import lombok.Data;
import java.util.Date;

@Data
public class GoodsVo {
    private Long id;            // 商品ID
    private String goodsName;   // 商品名称
    private String goodsImg;    // 商品图片
    private Double goodsPrice;  // 原价
    private Double miaoshaPrice;// 秒杀价
    private Integer stockCount; // 秒杀库存
    private Date startDate;     // 秒杀开始时间
    private Date endDate;       // 秒杀结束时间
}

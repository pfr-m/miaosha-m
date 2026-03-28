package com.miaosha.m.controller;

import com.miaosha.m.entity.MiaoshaUser;
import com.miaosha.m.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/goods")
public class GoodsController {

    @Autowired
    GoodsService goodsService;

    @RequestMapping("/to_list")
    public String toList(Model model, MiaoshaUser user) {
        // 1. 将用户信息传给前端（用于显示头像或昵称）
        model.addAttribute("user", user);

        // 2. 查询商品列表（包含秒杀价格、库存、时间等）
        // 这里需要你创建一个 GoodsService 和对应的 Vo
        // model.addAttribute("goodsList", goodsService.listGoodsVo());

        return "goods_list"; // 对应 templates/goods_list.html
    }
}

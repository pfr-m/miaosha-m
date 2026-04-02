package com.miaosha.m.controller;

import com.miaosha.m.result.Result;
import com.miaosha.m.result.CodeMsg;
import com.miaosha.m.entity.MiaoshaUser;
import com.miaosha.m.entity.MiaoshaOrder;
import com.miaosha.m.vo.GoodsVo;
import com.miaosha.m.redis.RedisService;
import com.miaosha.m.redis.GoodsKey;
import com.miaosha.m.service.GoodsService;
import com.miaosha.m.service.OrderService;
import com.miaosha.m.rabbitmq.MQSender;
import com.miaosha.m.rabbitmq.MiaoshaMessage;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;

@Controller
@RequestMapping("/miaosha")
public class MiaoshaController implements InitializingBean {

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private RedisService redisService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private MQSender mqSender;

    // 本地内存标记 (Map)，简历亮点：减少对 Redis 的穿透
    private HashMap<Long, Boolean> localOverMap = new HashMap<>();

    /**
     * 系统初始化：将秒杀商品库存预热到 Redis
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        List<GoodsVo> goodsList = goodsService.listGoodsVo();
        if (goodsList == null) return;
        for (GoodsVo goods : goodsList) {
            // 预热库存到 Redis
            redisService.set(GoodsKey.getMiaoshaGoodsStock, "" + goods.getId(), goods.getStockCount());
            // 初始化内存标记为 false（即还有货）
            localOverMap.put(goods.getId(), false);
        }
    }

    @PostMapping("/do_miaosha")
    @ResponseBody
    public Result<Integer> doMiaosha(MiaoshaUser user, @RequestParam("goodsId") long goodsId) {
        if (user == null) return Result.error(CodeMsg.SESSION_ERROR);

        // 1. 本地内存标记拦截：如果内存标记已结束，直接返回，不访问 Redis
        if (localOverMap.get(goodsId)) {
            return Result.error(CodeMsg.MIAO_SHA_OVER);
        }

        // 2. Redis 预减库存 (原子操作)
        long stock = redisService.decr(GoodsKey.getMiaoshaGoodsStock, "" + goodsId);
        if (stock < 0) {
            localOverMap.put(goodsId, true); // 打上内存标记，表示已售罄
            return Result.error(CodeMsg.MIAO_SHA_OVER);
        }

        // 3. 判断是否重复秒杀（从缓存获取订单信息）
        MiaoshaOrder order = orderService.getMiaoshaOrderByUserIdGoodsId(user.getId(), goodsId);
        if (order != null) {
            return Result.error(CodeMsg.REPEATE_MIAOSHA);
        }

        // 4. 入队：发送异步消息到 RabbitMQ 实现“异步削峰”
        MiaoshaMessage mm = new MiaoshaMessage();
        mm.setUser(user);
        mm.setGoodsId(goodsId);
        mqSender.sendMiaoshaMessage(mm);

        return Result.success(0); // 0 代表排队中
    }
}

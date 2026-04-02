package com.miaosha.m.service;

import com.miaosha.m.mapper.GoodsMapper;
import com.miaosha.m.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class GoodsService {

    @Autowired
    GoodsMapper goodsMapper;

    public List<GoodsVo> listGoodsVo() {
        return goodsMapper.listGoodsVo();
    }
    public boolean reduceStock(GoodsVo goods) {
        int ret = goodsMapper.reduceStock(goods);
        return ret > 0;
    }
}

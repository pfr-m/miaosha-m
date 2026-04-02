package com.miaosha.m.mapper;

import com.miaosha.m.vo.GoodsVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface GoodsMapper {

    @Select("select g.*, mg.stock_count, mg.start_date, mg.end_date, mg.miaosha_price " +
            "from miaosha_goods mg left join goods g on mg.goods_id = g.id")
    List<GoodsVo> listGoodsVo();
    // 核心：利用 MySQL 行锁保证库存扣减的原子性
    @Update("update miaosha_goods set stock_count = stock_count - 1 where goods_id = #{id} and stock_count > 0")
    int reduceStock(GoodsVo goods);
}

package com.shallowan.seckill.vo;

import com.shallowan.seckill.domain.SeckillUser;
import lombok.Data;

/**
 * @author ShallowAn
 */
@Data
public class GoodsDetailVO {

    private int seckillStatus = 0;
    private int remainSeconds = 0;
    private GoodsVO goods;
    private SeckillUser seckillUser;

    public int getSeckillStatus() {
        return seckillStatus;
    }

    public void setSeckillStatus(int seckillStatus) {
        this.seckillStatus = seckillStatus;
    }

    public int getRemainSeconds() {
        return remainSeconds;
    }

    public void setRemainSeconds(int remainSeconds) {
        this.remainSeconds = remainSeconds;
    }

    public GoodsVO getGoods() {
        return goods;
    }

    public void setGoods(GoodsVO goods) {
        this.goods = goods;
    }

    public SeckillUser getSeckillUser() {
        return seckillUser;
    }

    public void setSeckillUser(SeckillUser seckillUser) {
        this.seckillUser = seckillUser;
    }

    public GoodsDetailVO(int seckillStatus, int remainSeconds, GoodsVO goods, SeckillUser seckillUser) {
        this.seckillStatus = seckillStatus;
        this.remainSeconds = remainSeconds;
        this.goods = goods;
        this.seckillUser = seckillUser;
    }

    public GoodsDetailVO() {
    }

    @Override
    public String toString() {
        return "GoodsDetailVO{" +
                "seckillStatus=" + seckillStatus +
                ", remainSeconds=" + remainSeconds +
                ", goods=" + goods +
                ", seckillUser=" + seckillUser +
                '}';
    }
}

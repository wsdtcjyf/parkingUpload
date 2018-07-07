package com.shallowan.seckill.rabbitmq;

import com.shallowan.seckill.domain.SeckillUser;
import lombok.Data;

/**
 * @author ShallowAn
 */
@Data
public class SeckillMessage {
    private SeckillUser seckillUser;
    private long goodsId;

    public SeckillUser getSeckillUser() {
        return seckillUser;
    }

    public void setSeckillUser(SeckillUser seckillUser) {
        this.seckillUser = seckillUser;
    }

    public long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(long goodsId) {
        this.goodsId = goodsId;
    }

    @Override
    public String toString() {
        return "SeckillMessage{" +
                "seckillUser=" + seckillUser +
                ", goodsId=" + goodsId +
                '}';
    }
}

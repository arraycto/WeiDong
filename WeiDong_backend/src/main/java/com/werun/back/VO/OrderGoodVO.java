package com.werun.back.VO;

import com.werun.back.entity.GoodsEntity;
import com.werun.back.entity.OrderEntity;

import java.io.Serializable;

/**
 * @ClassName OrderGoodVO
 * @Author HWG
 * @Time 2019/4/21 9:44
 */

public class OrderGoodVO extends OrderEntity implements Serializable {
    private static final long serialVersionUID = 4946671614908689016L;

    private GoodsEntity good;

    public GoodsEntity getGood() {
        return good;
    }

    public void setGood(GoodsEntity good) {
        this.good = good;
    }

    public OrderGoodVO() {
    }
    public OrderGoodVO(OrderEntity order) {
        super(order);
    }
    public OrderGoodVO(OrderGoodVO order) {
        super((OrderEntity) order);
        this.good=order.getGood();
    }
}

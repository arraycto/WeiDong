package com.werun.back.VO;

import com.werun.back.entity.GoodsEntity;
import com.werun.back.entity.UserBody;
import com.werun.back.entity.UserEntity;

import java.io.Serializable;

/**
 * @ClassName GoodsUserEntity
 * @Author HWG
 * @Time 2019/4/21 8:48
 */

public class GoodsUserEntity extends GoodsEntity implements Serializable {

    private static final long serialVersionUID = 8668269041825633003L;
    private UserEntity user;

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public GoodsUserEntity() {
    }
    public GoodsUserEntity(GoodsEntity goodsEntity) {
        super(goodsEntity);
    }

    public GoodsUserEntity(GoodsUserEntity goodUser) {
        super((GoodsEntity)goodUser);
        this.user=goodUser.getUser();
    }
}

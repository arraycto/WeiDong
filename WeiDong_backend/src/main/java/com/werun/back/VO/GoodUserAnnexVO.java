package com.werun.back.VO;

import com.werun.back.entity.GoodsEntity;
import com.werun.back.entity.UserEntity;

import java.io.Serializable;
import java.util.List;

/**
 * @ClassName GoodUserAnnexVO
 * @Author HWG
 * @Time 2019/4/21 9:01
 */

public class GoodUserAnnexVO extends GoodsUserEntity implements Serializable {
    private static final long serialVersionUID = -5879513977311698623L;

    List<String> annex;

    public List<String> getAnnex() {
        return annex;
    }

    public void setAnnex(List<String> annex) {
        this.annex = annex;
    }

    public GoodUserAnnexVO() {
    }
    public GoodUserAnnexVO(GoodsUserEntity goodUser) {
        super(goodUser);
    }
}

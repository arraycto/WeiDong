package com.werun.back.VO;

import com.werun.back.entity.ActivityEntity;
import com.werun.back.entity.UserEntity;

import java.io.Serializable;

/**
 * @ClassName ActUserVO
 * @Author HWG
 * @Time 2019/4/23 9:44
 */

public class ActUserVO extends ActivityEntity implements Serializable {
    private static final long serialVersionUID = -2643472137829488183L;
    private UserEntity user;
    private int repository;     //与活动的关系 1未加入 2已加入 3创始人

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public int getRepository() {
        return repository;
    }


    public void setRepository(int repository) {
        this.repository = repository;
    }

    public ActUserVO() {
    }

    public ActUserVO(ActivityEntity act) {
        super(act);
    }

    public ActUserVO(ActUserVO act) {
        super((ActivityEntity) act);
        this.user=act.getUser();
        this.repository=act.getRepository();
    }
}

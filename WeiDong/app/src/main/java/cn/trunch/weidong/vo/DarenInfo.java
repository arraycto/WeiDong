package cn.trunch.weidong.vo;


import java.io.Serializable;

import cn.trunch.weidong.entity.UserEntity;

/**
 * @ClassName DarenInfo
 * @Author HWG
 * @Time 2019/5/1 23:06
 */

public class DarenInfo implements Serializable  {
    private static final long serialVersionUID = -2167965304612097962L;
    private int likeNum;
    private int answerNum;
    private UserEntity user;

    public int getLikeNum() {
        return likeNum;
    }

    public void setLikeNum(int likeNum) {
        this.likeNum = likeNum;
    }

    public int getAnswerNum() {
        return answerNum;
    }

    public void setAnswerNum(int answerNum) {
        this.answerNum = answerNum;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public DarenInfo() {
    }

    public DarenInfo(UserEntity user) {
        this.user = user;
    }
}

package cn.trunch.weidong.vo;


import java.io.Serializable;

import cn.trunch.weidong.entity.CommentEntity;
import cn.trunch.weidong.entity.UserEntity;

/**
 * @ClassName ComUserVO
 * @Author HWG
 * @Time 2019/4/23 17:16
 */

public class ComUserVO extends CommentEntity implements Serializable {
    private static final long serialVersionUID = 7303548370899898267L;
    private UserEntity user;
    private int repository; //0为点赞  一点咱

    public int getRepository() {
        return repository;
    }

    public void setRepository(int repository) {
        this.repository = repository;
    }

    public ComUserVO(CommentEntity com) {
        super(com);
        this.repository=0;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public ComUserVO() {
        this.repository=0;
    }
}

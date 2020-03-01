package cn.trunch.weidong.vo;

import java.io.Serializable;

import cn.trunch.weidong.entity.TeamEntity;
import cn.trunch.weidong.entity.UserEntity;

/**
 * @ClassName ActUserVO
 * @Author HWG
 * @Time 2019/4/23 9:44
 */

public class TeamUserVO extends TeamEntity implements Serializable {
    private static final long serialVersionUID = -2643472137829488183L;
    private UserEntity user;
    private int repository;     //与小组的关系 1未加入 2已加入 3已退出  4创始人

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

    public TeamUserVO() {
    }

    public TeamUserVO(TeamEntity team) {
        super(team);
    }

    public TeamUserVO(TeamUserVO team) {
        super((TeamEntity) team);
        this.user=team.getUser();
        this.repository=team.getRepository();
    }
}

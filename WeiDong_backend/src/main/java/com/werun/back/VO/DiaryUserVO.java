package com.werun.back.VO;

import com.werun.back.entity.DiaryEntity;
import com.werun.back.entity.UserEntity;

import javax.swing.text.DefaultHighlighter;
import java.io.Serializable;
import java.util.List;

/**
 * @ClassName DiaryUserVO
 * @Author HWG
 * @Time 2019/4/22 9:55
 */

public class DiaryUserVO extends DiaryEntity implements Serializable {

    private static final long serialVersionUID = 8649420209386553539L;

    private UserEntity user;
    private UserEntity buser;
    private List<String> img;
    private int isLike;

    public int getRepositity() {
        return repositity;
    }

    public void setRepositity(int repositity) {
        this.repositity = repositity;
    }

    private int repositity;
    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public UserEntity getBuser() {
        return buser;
    }

    public void setBuser(UserEntity buser) {
        this.buser = buser;
    }

    public List<String> getImg() {
        return img;
    }

    public void setImg(List<String> img) {
        this.img = img;
    }

    public DiaryUserVO(DiaryEntity diary) {
        super(diary);
    }
    public DiaryUserVO(DiaryUserVO diary) {
        super((DiaryEntity)diary);
        this.user=diary.getUser();
        this.buser=diary.getBuser();
        this.img=diary.getImg();
        this.repositity=diary.getRepositity();
        this.isLike=diary.getIsLike();
    }
    public DiaryUserVO() {}

    public int getIsLike() {
        return isLike;
    }

    public void setIsLike(int isLike) {
        this.isLike = isLike;
    }
}

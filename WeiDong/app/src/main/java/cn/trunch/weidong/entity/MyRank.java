package cn.trunch.weidong.entity;

import java.io.Serializable;

/**
 * @ClassName MyRank
 * @Author HWG
 * @Time 2019/5/2 9:58
 */

public class MyRank implements Serializable {
    private static final long serialVersionUID = 2420214918726751716L;
    private int worldRank;      //世界排名
    private int schoolRank;     //同校排名
    private int myRank;          //我的段位

    public int getWorldRank() {
        return worldRank;
    }

    public void setWorldRank(int worldRank) {
        this.worldRank = worldRank;
    }

    public int getSchoolRank() {
        return schoolRank;
    }

    public void setSchoolRank(int schoolRank) {
        this.schoolRank = schoolRank;
    }

    public int getMyRank() {
        return myRank;
    }

    public void setMyRank(int myRank) {
        this.myRank = myRank;
    }

    public MyRank() {
    }

    public MyRank(int worldRank, int schoolRank, int myRank) {
        this.worldRank = worldRank;
        this.schoolRank = schoolRank;
        this.myRank = myRank;
    }
}

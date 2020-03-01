package cn.trunch.weidong.entity;


import java.io.Serializable;

public class UserBody implements Serializable {

    private static final long serialVersionUID = -4311262987788898738L;
    private String uId;
  private int uHeight;
  private int uWeight;
  private int uVialCap;
  private String uChangeTime;

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }

    public int getuHeight() {
        return uHeight;
    }

    public void setuHeight(int uHeight) {
        this.uHeight = uHeight;
    }

    public int getuWeight() {
        return uWeight;
    }

    public void setuWeight(int uWeight) {
        this.uWeight = uWeight;
    }

    public int getuVialCap() {
        return uVialCap;
    }

    public void setuVialCap(int uVialCap) {
        this.uVialCap = uVialCap;
    }

    public String getuChangeTime() {
        return uChangeTime;
    }

    public void setuChangeTime(String uChangeTime) {
        this.uChangeTime = uChangeTime;
    }

    public UserBody() {
    }

    public UserBody(String uId) {
        this.uId = uId;
    }
}

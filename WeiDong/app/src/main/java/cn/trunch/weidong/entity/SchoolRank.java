package cn.trunch.weidong.entity;


import java.io.Serializable;

public class SchoolRank implements Serializable {

  private static final long serialVersionUID = -4907025464963198251L;
  private String sName;
  private int sStuNum;
  private int sExTime;
  private int sHistoryNum;
  private String sUpdateTime;

  public SchoolRank() {
  }

  public String getsName() {
    return sName;
  }

  public void setsName(String sName) {
    this.sName = sName;
  }

  public int getsStuNum() {
    return sStuNum;
  }

  public void setsStuNum(int sStuNum) {
    this.sStuNum = sStuNum;
  }

  public int getsExTime() {
    return sExTime;
  }

  public void setsExTime(int sExTime) {
    this.sExTime = sExTime;
  }

  public int getsHistoryNum() {
    return sHistoryNum;
  }

  public void setsHistoryNum(int sHistoryNum) {
    this.sHistoryNum = sHistoryNum;
  }

  public String getsUpdateTime() {
    return sUpdateTime;
  }

  public void setsUpdateTime(String sUpdateTime) {
    this.sUpdateTime = sUpdateTime;
  }
}

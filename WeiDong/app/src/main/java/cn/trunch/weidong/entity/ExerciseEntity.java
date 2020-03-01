package cn.trunch.weidong.entity;


import java.io.Serializable;

public class ExerciseEntity implements Serializable {

    private static final long serialVersionUID = -1252472436101905123L;
    private String exId;
  private String uId;
  private int exType;
  private int exAmount;
  private String exLocationLatitude;
  private String exLocationLongitude;
  private String exStartTime;
  private String exEndTime;

    public String getExId() {
        return exId;
    }

    public void setExId(String exId) {
        this.exId = exId;
    }

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }

    public int getExType() {
        return exType;
    }

    public void setExType(int exType) {
        this.exType = exType;
    }

    public int getExAmount() {
        return exAmount;
    }

    public void setExAmount(int exAmount) {
        this.exAmount = exAmount;
    }

    public String getExLocationLatitude() {
        return exLocationLatitude;
    }

    public void setExLocationLatitude(String exLocationLatitude) {
        this.exLocationLatitude = exLocationLatitude;
    }

    public String getExLocationLongitude() {
        return exLocationLongitude;
    }

    public void setExLocationLongitude(String exLocationLongitude) {
        this.exLocationLongitude = exLocationLongitude;
    }

    public String getExStartTime() {
        return exStartTime;
    }

    public void setExStartTime(String exStartTime) {
        this.exStartTime = exStartTime;
    }

    public String getExEndTime() {
        return exEndTime;
    }

    public void setExEndTime(String exEndTime) {
        this.exEndTime = exEndTime;
    }

    public ExerciseEntity() {
    }
}

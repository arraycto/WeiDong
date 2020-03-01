package cn.trunch.weidong.entity;


import java.io.Serializable;

public class UserSchool implements Serializable {

  private static final long serialVersionUID = 6779692774917482655L;
  private String uId;
  private String uSchool;
  private String uAcademy;
  private String uNumber;
  private int uRegYear;
  private String uEdu;
  private String uChangeTime;
  private String uImg;

  public String getuId() {
    return uId;
  }

  public void setuId(String uId) {
    this.uId = uId;
  }

  public String getuSchool() {
    return uSchool;
  }

  public void setuSchool(String uSchool) {
    this.uSchool = uSchool;
  }

  public String getuAcademy() {
    return uAcademy;
  }

  public void setuAcademy(String uAcademy) {
    this.uAcademy = uAcademy;
  }

  public String getuNumber() {
    return uNumber;
  }

  public void setuNumber(String uNumber) {
    this.uNumber = uNumber;
  }

  public int getuRegYear() {
    return uRegYear;
  }

  public void setuRegYear(int uRegYear) {
    this.uRegYear = uRegYear;
  }

  public String getuEdu() {
    return uEdu;
  }

  public void setuEdu(String uEdu) {
    this.uEdu = uEdu;
  }

  public String getuChangeTime() {
    return uChangeTime;
  }

  public void setuChangeTime(String uChangeTime) {
    this.uChangeTime = uChangeTime;
  }

  public String getuImg() {
    return uImg;
  }

  public void setuImg(String uImg) {
    this.uImg = uImg;
  }

  public UserSchool() {
  }

  public UserSchool(String uId) {
    this.uId = uId;
  }
}

package cn.trunch.weidong.entity;


import java.io.Serializable;

public class TeamEntity implements Serializable {

  private static final long serialVersionUID = 4032422042586551411L;
  private String teamId;
  private String teamUid;     //队长
  private int teamType;
  private String teamName;
  private String teamDesc;    //简介
  private String teamLocation;
  private String teamTime;  //建立时间
  private int actUserNum;
  private int teamStatus;
  private String actAvatar; //头像

  public TeamEntity() {
  }
  public TeamEntity(TeamEntity team) {
    this.teamId=team.getTeamId();
    this.teamUid=team.getTeamUid();
    this.teamType=team.getTeamType();
    this.teamName=team.getTeamName();
    this.teamDesc=team.getTeamDesc();
    this.teamLocation=team.getTeamLocation();
    this.teamTime=team.getTeamTime();
    this.actUserNum=team.getActUserNum();
    this.teamStatus=team.getTeamStatus();
    this.actAvatar=team.getActAvatar();
  }

  public String getTeamId() {
    return teamId;
  }

  public void setTeamId(String teamId) {
    this.teamId = teamId;
  }

  public String getTeamUid() {
    return teamUid;
  }

  public void setTeamUid(String teamUid) {
    this.teamUid = teamUid;
  }

  public int getTeamType() {
    return teamType;
  }

  public void setTeamType(int teamType) {
    this.teamType = teamType;
  }

  public String getTeamName() {
    return teamName;
  }

  public void setTeamName(String teamName) {
    this.teamName = teamName;
  }

  public String getTeamDesc() {
    return teamDesc;
  }

  public void setTeamDesc(String teamDesc) {
    this.teamDesc = teamDesc;
  }

  public String getTeamLocation() {
    return teamLocation;
  }

  public void setTeamLocation(String teamLocation) {
    this.teamLocation = teamLocation;
  }

  public String getTeamTime() {
    return teamTime;
  }

  public void setTeamTime(String teamTime) {
    this.teamTime = teamTime;
  }

  public int getActUserNum() {
    return actUserNum;
  }

  public void setActUserNum(int actUserNum) {
    this.actUserNum = actUserNum;
  }

  public int getTeamStatus() {
    return teamStatus;
  }

  public void setTeamStatus(int teamStatus) {
    this.teamStatus = teamStatus;
  }

  public String getActAvatar() {
    return actAvatar;
  }

  public void setActAvatar(String actAvatar) {
    this.actAvatar = actAvatar;
  }
}

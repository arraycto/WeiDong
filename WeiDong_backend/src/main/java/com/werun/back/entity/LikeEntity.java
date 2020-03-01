package com.werun.back.entity;


import java.io.Serializable;

public class LikeEntity implements Serializable {

  private static final long serialVersionUID = -5726096081325330256L;
  private String likeItemId;
  private String likeUid;
  private int likeType;
  private String likeTime;
  private int likeStatus;

  public String getLikeItemId() {
    return likeItemId;
  }

  public void setLikeItemId(String likeItemId) {
    this.likeItemId = likeItemId;
  }

  public String getLikeUid() {
    return likeUid;
  }

  public void setLikeUid(String likeUid) {
    this.likeUid = likeUid;
  }

  public int getLikeType() {
    return likeType;
  }

  public void setLikeType(int likeType) {
    this.likeType = likeType;
  }

  public String getLikeTime() {
    return likeTime;
  }

  public void setLikeTime(String likeTime) {
    this.likeTime = likeTime;
  }

  public int getLikeStatus() {
    return likeStatus;
  }

  public void setLikeStatus(int likeStatus) {
    this.likeStatus = likeStatus;
  }

  public LikeEntity() {
  }

  public LikeEntity(String likeItemId, String likeUid, int likeType) {
    this.likeItemId = likeItemId;
    this.likeUid = likeUid;
    this.likeType = likeType;
  }

  public LikeEntity(LikeEntity like) {
      this.likeItemId=like.getLikeItemId();
      this.likeUid=like.getLikeUid();
      this.likeType=like.getLikeType();
      this.likeTime=like.getLikeTime();
      this.likeStatus=like.getLikeStatus();
  }
}

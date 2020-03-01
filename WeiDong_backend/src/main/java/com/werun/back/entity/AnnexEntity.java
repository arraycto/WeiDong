package com.werun.back.entity;

import java.io.Serializable;

public class AnnexEntity implements Serializable {

  private static final long serialVersionUID = -7704788245434426825L;
  private String postId;
  private String annexName;
  private int annexOrder;

  @Override
  public String toString() {
    return "AnnexEntity{" +
            "postId='" + postId + '\'' +
            ", annexName='" + annexName + '\'' +
            ", annexOrder=" + annexOrder +
            '}';
  }

  public String getPostId() {
    return postId;
  }

  public void setPostId(String postId) {
    this.postId = postId;
  }

  public String getAnnexName() {
    return annexName;
  }

  public void setAnnexName(String annexName) {
    this.annexName = annexName;
  }

  public int getAnnexOrder() {
    return annexOrder;
  }

  public void setAnnexOrder(int annexOrder) {
    this.annexOrder = annexOrder;
  }

  public AnnexEntity(String postId, String annexName, int annexOrder) {
    this.postId = postId;
    this.annexName = annexName;
    this.annexOrder = annexOrder;
  }

  public AnnexEntity() {
  }
}

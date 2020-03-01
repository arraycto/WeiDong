package cn.trunch.weidong.entity;


import java.io.Serializable;
import java.util.List;

public class CommentEntity implements Serializable {

  private static final long serialVersionUID = 385600684357637778L;
  private String comId;
  private String comItemId;
  private String comUid;
  private String comBUid;
  private int comType;
  private String comContent;
  private int comLikeNum;
  private String comTime;
  private List<String> imgs;

  public List<String> getImgs() {
    return imgs;
  }

  public void setImgs(List<String> imgs) {
    this.imgs = imgs;
  }

  public String getComId() {
    return comId;
  }

  public void setComId(String comId) {
    this.comId = comId;
  }

  public String getComItemId() {
    return comItemId;
  }

  public void setComItemId(String comItemId) {
    this.comItemId = comItemId;
  }

  public String getComUid() {
    return comUid;
  }

  public void setComUid(String comUid) {
    this.comUid = comUid;
  }

  public String getComBUid() {
    return comBUid;
  }

  public void setComBUid(String comBUid) {
    this.comBUid = comBUid;
  }

  public int getComType() {
    return comType;
  }

  public void setComType(int comType) {
    this.comType = comType;
  }

  public String getComContent() {
    return comContent;
  }

  public void setComContent(String comContent) {
    this.comContent = comContent;
  }

  public int getComLikeNum() {
    return comLikeNum;
  }

  public void setComLikeNum(int comLikeNum) {
    this.comLikeNum = comLikeNum;
  }

  public String getComTime() {
    return comTime;
  }

  public void setComTime(String comTime) {
    this.comTime = comTime;
  }

  public CommentEntity() {
  }
  public CommentEntity(CommentEntity com){
    this.comId=com.getComId();
    this.comItemId=com.getComItemId();
    this.comUid=com.getComUid();
    this.comBUid=com.getComBUid();
    this.comType=com.getComType();
    this.comContent=com.getComContent();
    this.comLikeNum=com.getComLikeNum();
    this.comTime=com.getComTime();
    this.imgs=com.getImgs();
  }
}

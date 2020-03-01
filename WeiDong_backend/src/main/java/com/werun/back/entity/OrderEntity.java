package com.werun.back.entity;


import java.io.Serializable;

public class OrderEntity implements Serializable {

  private static final long serialVersionUID = -7893202368004817845L;
  private String oId;
  private String oUid;
  private String oGid;
  private int oPrice;
  private int oNum;
  private String oTime;
  private int oStatus;
  private String oAddress;

  @Override
  public String toString() {
    return "OrderEndity{" +
            "oId='" + oId + '\'' +
            ", oUid='" + oUid + '\'' +
            ", oGid='" + oGid + '\'' +
            ", oPrice=" + oPrice +
            ", oNum=" + oNum +
            ", oTime='" + oTime + '\'' +
            ", oStatus=" + oStatus +
            ", oAddress='" + oAddress + '\'' +
            '}';
  }

  public String getoId() {
    return oId;
  }

  public void setoId(String oId) {
    this.oId = oId;
  }

  public String getoUid() {
    return oUid;
  }

  public void setoUid(String oUid) {
    this.oUid = oUid;
  }

  public String getoGid() {
    return oGid;
  }

  public void setoGid(String oGid) {
    this.oGid = oGid;
  }

  public int getoPrice() {
    return oPrice;
  }

  public void setoPrice(int oPrice) {
    this.oPrice = oPrice;
  }

  public int getoNum() {
    return oNum;
  }

  public void setoNum(int oNum) {
    this.oNum = oNum;
  }

  public String getoTime() {
    return oTime;
  }

  public void setoTime(String oTime) {
    this.oTime = oTime;
  }

  public int getoStatus() {
    return oStatus;
  }

  public void setoStatus(int oStatus) {
    this.oStatus = oStatus;
  }

  public String getoAddress() {
    return oAddress;
  }

  public void setoAddress(String oAddress) {
    this.oAddress = oAddress;
  }

  public OrderEntity() {
  }

  public OrderEntity(OrderEntity order){
    this.oId=order.getoId();
    this.oUid=order.getoUid();
    this.oGid=order.getoGid();
    this.oPrice=order.getoPrice();
    this.oNum=order.getoNum();
    this.oTime=order.getoTime();
    this.oStatus=order.getoStatus();
    this.oAddress=order.getoAddress();
  }
}

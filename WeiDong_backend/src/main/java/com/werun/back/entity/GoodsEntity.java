package com.werun.back.entity;


import java.io.Serializable;

public class GoodsEntity implements Serializable {

  private static final long serialVersionUID = 8023555720537708034L;
  private String gId;
  private String gUid;
  private int gPrice;
  private int gOldprice;
  private String gName;
  private String gPreviewImg;
  private String gDesc;
  private int gStock;
  private int gSaleNum;
  private int gType;
  private int gStatus;
  private String gTime;


    @Override
    public String toString() {
        return "GoodsEntity{" +
                "gId='" + gId + '\'' +
                ", gUid='" + gUid + '\'' +
                ", gPrice=" + gPrice +
                ", gName='" + gName + '\'' +
                ", gPreviewImg='" + gPreviewImg + '\'' +
                ", gDesc='" + gDesc + '\'' +
                ", gStock=" + gStock +
                ", gSaleNum=" + gSaleNum +
                ", gType=" + gType +
                ", gStatus=" + gStatus +
                ", gTime='" + gTime + '\'' +
                '}';
    }

    public GoodsEntity() {
  }

    public GoodsEntity(GoodsEntity good) {
      this.gId=good.getgId();
      this.gUid=good.getgUid();
      this.gPrice=good.getgPrice();
      this.gOldprice=good.getgOldprice();
      this.gName=good.getgName();
      this.gPreviewImg=good.getgPreviewImg();
      this.gDesc=good.getgDesc();
      this.gStock=good.getgStock();
      this.gSaleNum=good.getgSaleNum();
      this.gType=good.getgType();
      this.gStatus=good.getgStatus();
      this.gTime=good.getgTime();
    }
  public String getgId() {
    return gId;
  }

  public int getgOldprice() {
    return gOldprice;
  }

  public void setgOldprice(int gOldprice) {
    this.gOldprice = gOldprice;
  }

  public void setgId(String gId) {
    this.gId = gId;
  }

  public int getgPrice() {
    return gPrice;
  }

  public void setgPrice(int gPrice) {
    this.gPrice = gPrice;
  }

  public String getgName() {
    return gName;
  }

  public void setgName(String gName) {
    this.gName = gName;
  }

  public String getgPreviewImg() {
    return gPreviewImg;
  }

  public void setgPreviewImg(String gPreviewImg) {
    this.gPreviewImg = gPreviewImg;
  }

  public String getgDesc() {
    return gDesc;
  }

  public void setgDesc(String gDesc) {
    this.gDesc = gDesc;
  }

  public int getgStock() {
    return gStock;
  }

  public void setgStock(int gStock) {
    this.gStock = gStock;
  }


  public String getgUid() {
    return gUid;
  }

  public void setgUid(String gUid) {
    this.gUid = gUid;
  }

  public int getgSaleNum() {
    return gSaleNum;
  }

  public void setgSaleNum(int gSaleNum) {
    this.gSaleNum = gSaleNum;
  }

  public int getgType() {
    return gType;
  }

  public void setgType(int gType) {
    this.gType = gType;
  }

  public int getgStatus() {
    return gStatus;
  }

  public void setgStatus(int gStatus) {
    this.gStatus = gStatus;
  }

  public String getgTime() {
    return gTime;
  }

  public void setgTime(String gTime) {
    this.gTime = gTime;
  }
}

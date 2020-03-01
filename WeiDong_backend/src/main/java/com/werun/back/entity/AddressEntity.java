package com.werun.back.entity;


import java.io.Serializable;

public class AddressEntity implements Serializable {

  private static final long serialVersionUID = 3684415987629901291L;
  private String adId;
  private String adUid;
  private String adName;
  private String adPhone;
  private String adProvince;
  private String adCity;
  private String adCounty;
  private String adDtl;
  private int adStatus;
  private int adDefault;

  public String getAdId() {
    return adId;
  }

  public void setAdId(String adId) {
    this.adId = adId;
  }

  public String getAdUid() {
    return adUid;
  }

  public void setAdUid(String adUid) {
    this.adUid = adUid;
  }

  public String getAdName() {
    return adName;
  }

  public void setAdName(String adName) {
    this.adName = adName;
  }

  public String getAdPhone() {
    return adPhone;
  }

  public void setAdPhone(String adPhone) {
    this.adPhone = adPhone;
  }

  public String getAdProvince() {
    return adProvince;
  }

  public void setAdProvince(String adProvince) {
    this.adProvince = adProvince;
  }

  public String getAdCity() {
    return adCity;
  }

  public void setAdCity(String adCity) {
    this.adCity = adCity;
  }

  public String getAdCounty() {
    return adCounty;
  }

  public void setAdCounty(String adCounty) {
    this.adCounty = adCounty;
  }

  public String getAdDtl() {
    return adDtl;
  }

  public void setAdDtl(String adDtl) {
    this.adDtl = adDtl;
  }

  public int getAdStatus() {
    return adStatus;
  }

  public void setAdStatus(int adStatus) {
    this.adStatus = adStatus;
  }

  public int getAdDefault() {
    return adDefault;
  }

  public void setAdDefault(int adDefault) {
    this.adDefault = adDefault;
  }

  public AddressEntity() {
  }
}

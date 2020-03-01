package com.werun.back.entity;


import java.io.Serializable;

public class ActivityEntity implements Serializable {

    private static final long serialVersionUID = 7878450450869658048L;
    private String actId;
    private String actUid;
    private String actTid;
    private String actName;
    private String actDesc;
    private String actLocation;
    private String actStartTime;
    private int actUserNum;
    private String actEndTime;
    private String actSetTime;
    private int actStatus;
    private String actPreviewImg;

    public String getActTid() {
        return actTid;
    }

    public void setActTid(String actTid) {
        this.actTid = actTid;
    }

    public String getActSetTime() {
        return actSetTime;
    }

    public void setActSetTime(String actSetTime) {
        this.actSetTime = actSetTime;
    }

    public String getActId() {
        return actId;
    }

    public void setActId(String actId) {
        this.actId = actId;
    }

    public String getActUid() {
        return actUid;
    }

    public void setActUid(String actUid) {
        this.actUid = actUid;
    }


    public String getActName() {
        return actName;
    }

    public void setActName(String actName) {
        this.actName = actName;
    }

    public String getActDesc() {
        return actDesc;
    }

    public void setActDesc(String actDesc) {
        this.actDesc = actDesc;
    }

    public String getActLocation() {
        return actLocation;
    }

    public void setActLocation(String actLocation) {
        this.actLocation = actLocation;
    }

    public String getActStartTime() {
        return actStartTime;
    }

    public void setActStartTime(String actStartTime) {
        this.actStartTime = actStartTime;
    }

    public int getActUserNum() {
        return actUserNum;
    }

    public void setActUserNum(int actUserNum) {
        this.actUserNum = actUserNum;
    }

    public String getActEndTime() {
        return actEndTime;
    }

    public void setActEndTime(String actEndTime) {
        this.actEndTime = actEndTime;
    }

    public int getActStatus() {
        return actStatus;
    }

    public void setActStatus(int actStatus) {
        this.actStatus = actStatus;
    }

    public String getActPreviewImg() {
        return actPreviewImg;
    }

    public void setActPreviewImg(String actPreviewImg) {
        this.actPreviewImg = actPreviewImg;
    }

    public ActivityEntity() {
    }
    public ActivityEntity(ActivityEntity act){
        this.actId=act.getActId();
        this.actUid=act.getActUid();
        this.actTid=act.getActTid();
        this.actName=act.getActName();
        this.actDesc=act.getActDesc();
        this.actLocation=act.getActLocation();
        this.actStartTime=act.getActStartTime();
        this.actUserNum=act.getActUserNum();
        this.actEndTime=act.getActEndTime();
        this.actStatus=act.getActStatus();
        this.actPreviewImg=act.getActPreviewImg();
    }
}

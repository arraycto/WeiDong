package cn.trunch.weidong.entity;


public class ActivityEntity {
    private String actId;
    private String actUid;
    private int actType;
    private String actName;
    private String actDesc;
    private String actLocation;
    private String actStartTime;
    private int actUserNum;
    private String actEndTime;
    private int actStatus;
    private String actPreviewImg;

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

    public int getActType() {
        return actType;
    }

    public void setActType(int actType) {
        this.actType = actType;
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
}

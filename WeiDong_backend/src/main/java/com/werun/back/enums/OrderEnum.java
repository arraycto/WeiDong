package com.werun.back.enums;

/**
 * @ClassName OrderEnum
 * @Author HWG
 * @Time 2019/4/20 16:32
 */
public enum OrderEnum {
    NOPAY(1),
    NOSEND(2),
    NORECIEVE(3),
    NOCOMMENT(4),
    FINISH(5),
    DELETEDBYUSER(6),
    ;
    private int status;

    public int getStatus() {
        return status;
    }

    OrderEnum(int i) {
        this.status=i;
    }
}

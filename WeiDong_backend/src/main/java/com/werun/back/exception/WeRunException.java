package com.werun.back.exception;

import com.werun.back.enums.ExceptionsEnum;

/**
 * @ClassName WeRunException
 * @Author HWG
 * @Time 2019/4/17 14:37
 */

public class WeRunException extends RuntimeException {
    Integer code;
    String msg;

    public WeRunException(Integer code,String msg){
        super(msg);
        this.code=code;
        this.msg=msg;
    }

    public WeRunException(ExceptionsEnum exceptionsEnum) {
        this.code=exceptionsEnum.getCode();
        this.msg=exceptionsEnum.getMessage();
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}

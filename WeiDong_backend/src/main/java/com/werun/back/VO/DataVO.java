package com.werun.back.VO;

import com.werun.back.entity.PageInfo;

/**
 * @ClassName DataVO 统一返回格式
 * @Author HWG
 * @Time 2019/4/17 14:31
 */

public class DataVO<T> {
    private Integer code;
    private String msg;
    private PageInfo pageInfo;
    private T data;

    public DataVO(Integer code, String msg, PageInfo pageInfo, T data) {
        this.code = code;
        this.msg = msg;
        this.pageInfo = pageInfo;
        this.data = data;
    }

    public PageInfo getPageInfo() {
        return pageInfo;
    }

    public void setPageInfo(PageInfo pageInfo) {
        this.pageInfo = pageInfo;
    }

    public DataVO() {
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

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}

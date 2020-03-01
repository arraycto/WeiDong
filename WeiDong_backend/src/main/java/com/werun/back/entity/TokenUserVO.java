package com.werun.back.entity;

/**
 * @ClassName TokenUserVO
 * @Author HWG
 * @Time 2019/4/18 15:46
 */

public class TokenUserVO {
    String token;
    String u_id;

    @Override
    public String toString() {
        return "TokenUserVO{" +
                "token='" + token + '\'' +
                ", u_id='" + u_id + '\'' +
                '}';
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getU_id() {
        return u_id;
    }

    public void setU_id(String u_id) {
        this.u_id = u_id;
    }

    public TokenUserVO() {
    }

    public TokenUserVO(String token, String u_id) {
        this.token = token;
        this.u_id = u_id;
    }
}

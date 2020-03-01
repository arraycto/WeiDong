package com.werun.back.token;

import com.werun.back.entity.TokenUserVO;

/**
 * @ClassName TokenManager
 * @Author HWG
 * @Time 2019/4/18 15:45
 */
public interface TokenManager {

    TokenUserVO generateToken(String u_id);

    Boolean checkTokenIsValid(String token);

    String getUid(String token);

    Boolean updateToken(String token);

}

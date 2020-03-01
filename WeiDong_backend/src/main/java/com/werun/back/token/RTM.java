package com.werun.back.token;

import com.werun.back.entity.TokenUserVO;
import com.werun.back.entity.UserEntity;
import com.werun.back.utils.StrUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;


/**
 * @ClassName RTM
 * @Author HWG
 * @Time 2019/4/18 15:51
 */
@Service
public class RTM implements TokenManager {
    @Autowired
    private StringRedisTemplate srt;
    @Autowired
    private RedisTemplate<String, UserEntity> urt;

    @Override
    public TokenUserVO generateToken(String u_id) {
        String token= StrUtils.randomNum(false,10);
        srt.boundValueOps(token).set(u_id,10,TimeUnit.MINUTES);
        return new TokenUserVO(token,u_id);
    }

    @Override
    public Boolean checkTokenIsValid(String token) {
        String uid = srt.boundValueOps(token).get();
//        System.out.println(uid);
        if(uid==null||uid==""||uid.equals("null")){
            return false;
        }
        return true;
    }

    @Override
    public String getUid(String token) {
        return null;
    }

    @Override
    public Boolean updateToken(String token) {
        return null;
    }
}

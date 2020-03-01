package com.werun.back.service;

import com.werun.back.VO.DarenInfo;
import com.werun.back.dao.userDao;
import com.werun.back.entity.PageInfo;
import com.werun.back.entity.UserBody;
import com.werun.back.entity.UserEntity;
import com.werun.back.entity.UserSchool;
import com.werun.back.enums.ExceptionsEnum;
import com.werun.back.exception.WeRunException;
import com.werun.back.utils.StrUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName UserServ
 * @Author HWG
 * @Time 2019/4/19 13:18
 */
@Service
public class UserServ {
    @Resource
    userDao userdao;
    @Autowired
    @Qualifier("userRedisTemplate")
    private RedisTemplate<String, UserEntity> urt;

    @Value("${FileOp.werunImg.head}")
    private String headPath;

    public void insert(String phone) throws Exception {
        UserEntity u = new UserEntity();
        u.setuId(StrUtils.timeStamp() + StrUtils.randomNum(false, 5));
        u.setuPhone(phone);
        u.setuAvatar("default.jpg");
        u.setuNickname("用户"+phone.substring(phone.length()-5));
        try {
            userdao.insert(u);
        } catch (Exception e) {
            e.printStackTrace();
            throw new WeRunException(ExceptionsEnum.REGFAILED);
        }
        try {
            userdao.insertUserBody(new UserBody(u.getuId()));
        } catch (Exception e) {
            e.printStackTrace();
            throw new WeRunException(ExceptionsEnum.INSETUSERBODYFAILED);
        }
        try {
            userdao.insertUserSchool(new UserSchool(u.getuId()));
        } catch (Exception e) {
            e.printStackTrace();
            throw new WeRunException(ExceptionsEnum.INSETUSERSCHOOLFAILED);
        }
    }

    public UserEntity getByPhone(String phone) throws Exception {
        UserEntity user;
        try {
            user = userdao.getByPhone(phone);
            if (user == null)
                return null;
            user.setuAvatar(headPath + user.getuAvatar());
        } catch (Exception e) {
            e.printStackTrace();
            throw new WeRunException(ExceptionsEnum.NODATA);
        }
        return user;
    }

    public UserEntity getByUid(String uid) throws Exception {

        UserEntity user;
        try {
            user = userdao.getByUid(uid);
            user.setuAvatar(headPath + user.getuAvatar());
        } catch (Exception e) {
            e.printStackTrace();
            throw new WeRunException(ExceptionsEnum.NODATA);
        }
        return user;
    }

    public UserEntity getUserInRedis(String token) throws Exception {
        UserEntity user = urt.boundValueOps(token).get();
        if (user == null) {
            throw new WeRunException(ExceptionsEnum.NOLOGIN);
        }
        urt.boundValueOps(token).set(user, 72, TimeUnit.HOURS);
        return user;
    }

    //用户身体信息
    public UserBody getUserBodyByUid(String uid) throws Exception {
        UserBody i = userdao.getUserBodyByUid(uid);
        if (i == null)
            throw new WeRunException(ExceptionsEnum.NODATA);
        return i;
    }

    //用户单位信息
    public UserSchool getUserSchoolByUid(String uid) throws Exception {
        UserSchool userSchoolByUid = userdao.getUserSchoolByUid(uid);
        if (userSchoolByUid == null)
            throw new WeRunException(ExceptionsEnum.NODATA);
        if (userSchoolByUid.getuImg() != null)
            userSchoolByUid.setuImg(headPath + userSchoolByUid.getuImg());
        return userSchoolByUid;
    }

    public void updateUserBody(UserBody u) throws Exception {
        try {
            userdao.updateUBody(u);
        } catch (Exception e) {
            e.printStackTrace();
            throw new WeRunException(ExceptionsEnum.UPDATEUSERBODYFAILED);
        }
    }

    public void updateUserSchool(UserSchool u) throws Exception {
        try {
            userdao.updateUSchool(u);
        } catch (Exception e) {
            e.printStackTrace();
            throw new WeRunException(ExceptionsEnum.UPDATEUSERSCHOOLFAILED);
        }
    }

    public void updateUser(UserEntity u) throws Exception {
        try {
            userdao.updateUser(u);
        } catch (Exception e) {
            e.printStackTrace();
            throw new WeRunException(ExceptionsEnum.UPDATEUSERSCHOOLFAILED);
        }
    }

    public List<UserEntity> getAll(PageInfo page) throws Exception {
        try {
            List<UserEntity> all = userdao.getAll(page.getFromIndex(), page.getPageSize());
            if (all != null)
                for (UserEntity u : all)
                    u.setuAvatar(headPath + u.getuAvatar());
            return all;
        } catch (Exception e) {
            e.printStackTrace();
            throw new WeRunException(ExceptionsEnum.NOUSERDATA);
        }
    }

    public int countAll() throws Exception {
        try {
            return userdao.countAll();
        } catch (Exception e) {
            e.printStackTrace();
            throw new WeRunException(ExceptionsEnum.NOUSERDATA);
        }
    }

    public DarenInfo darenInfo(String uid) throws Exception {
        DarenInfo daren = new DarenInfo();
        try {
            UserEntity byUid = getByUid(uid);
            daren.setUser(byUid);
            int i = userdao.comLikeNum(uid);
            int i1 = userdao.diaryLikeNum(uid);
            daren.setLikeNum(i + i1);
            int i2 = userdao.answerNum(uid);
            daren.setAnswerNum(i2);
            int i3 = userdao.postNum(uid);
            daren.getUser().setuPostNum(i3);
        } catch (Exception e) {
            e.printStackTrace();
            throw new WeRunException(ExceptionsEnum.NOUSERDATA);
        }
        return daren;
    }
}

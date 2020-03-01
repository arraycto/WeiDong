package com.werun.back.controllor;

import com.werun.back.VO.DarenInfo;
import com.werun.back.VO.DataVO;
import com.werun.back.entity.PageInfo;
import com.werun.back.entity.UserBody;
import com.werun.back.entity.UserEntity;
import com.werun.back.entity.UserSchool;
import com.werun.back.enums.ExceptionsEnum;
import com.werun.back.exception.WeRunException;
import com.werun.back.service.SmsServ;
import com.werun.back.service.UserServ;
import com.werun.back.utils.Result;
import com.werun.back.utils.StrUtils;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * @ClassName userCon
 * @Author HWG
 * @Time 2019/4/19 12:34
 */
@Api(value = "用户操作API", tags = {"用户CRDU 接口"})
@Controller
@CrossOrigin
@RequestMapping("/user")
public class userCon {
    @Autowired
    private SmsServ smsServ;
    @Autowired
    private UserServ userServ;

    @PostMapping("/login")
    @ResponseBody
    public DataVO login(@RequestParam(value = "phone", required = true, defaultValue = "14769773649") String phone,
                        @RequestParam(value = "code", required = true, defaultValue = "123456") String code) throws Exception {
        String login = smsServ.login(phone, code);
        return Result.successToArray("token", login);
    }

    @PostMapping("/sendSms")
    @ResponseBody
    public DataVO send(@RequestParam(value = "phone", required = true, defaultValue = "14769773649") String phone) throws Exception {

        String code = StrUtils.randomNum(true, 6);

        ExceptionsEnum exceptionsEnum = smsServ.sendCode(phone, code);

        throw new WeRunException(exceptionsEnum);
    }

    @PostMapping("/sendTestSms")
    @ResponseBody
    public DataVO sendTest(@RequestParam(value = "phone", required = true, defaultValue = "14769773649") String phone) throws Exception {


        ExceptionsEnum exceptionsEnum = smsServ.sendTestCode(phone);

        throw new WeRunException(exceptionsEnum);
    }

    @PostMapping("/userInfo")
    @ResponseBody
    public DataVO userInfo(@RequestParam(value = "token", required = true, defaultValue = "000") String token) throws Exception {
        UserEntity userInRedis = userServ.getUserInRedis(token);
        userInRedis=userServ.getByUid(userInRedis.getuId());
        return Result.successToArray(userInRedis);
    }

    @PostMapping("/userBodyInfo")
    @ResponseBody
    public DataVO userBodyInfo(@RequestParam(value = "token", required = true, defaultValue = "000") String token) throws Exception {
        UserEntity user = userServ.getUserInRedis(token);
        UserBody body = userServ.getUserBodyByUid(user.getuId());
        return Result.successToArray(body);
    }

    @PostMapping("/userSchoolInfo")
    @ResponseBody
    public DataVO userSchoolInfo(@RequestParam(value = "token", required = true, defaultValue = "000") String token) throws Exception {
        UserEntity userInRedis = userServ.getUserInRedis(token);
        UserSchool uBodyByUid = userServ.getUserSchoolByUid(userInRedis.getuId());
        return Result.successToArray(uBodyByUid);
    }

    @PostMapping("/updateUser")
    @ResponseBody
    public DataVO updateUser(@RequestParam(value = "token", required = true) String token,
                             @RequestParam(value = "head", required = false, defaultValue = "-1") String head,
                             @RequestParam(value = "nickName", required = false, defaultValue = "-1") String nick,
                             @RequestParam(value = "selfdes", required = false, defaultValue = "-1") String selfdes,
                             @RequestParam(value = "gender",required = false,defaultValue = "-1")int sex,
                             @RequestParam(value = "birthday",required = false,defaultValue = "-1")String birthday,
                             @RequestParam(value = "height",required = false,defaultValue = "-1")int height,
                             @RequestParam(value = "weight",required = false,defaultValue = "-1")int weight,
                             @RequestParam(value = "vialCap",required = false,defaultValue = "-1")int vial) throws Exception {
        UserEntity user = userServ.getUserInRedis(token);
        user.setuAvatar(head);
        user.setuNickname(nick);
        user.setuSelfdes(selfdes);
        user.setuGender(sex);
        user.setuBirthday(birthday);
        UserBody userBody = new UserBody();
        userBody.setuId(user.getuId());
        userBody.setuHeight(height);
        userBody.setuWeight(weight);
        userBody.setuVialCap(vial);
        userServ.updateUserBody(userBody);
        userServ.updateUser(user);
        return Result.success();
    }

    @PostMapping("/updateUSchool")
    @ResponseBody
    public DataVO updateUSchool(@RequestParam(value = "token", required = true) String token,
                             @RequestParam(value = "school", required = false, defaultValue = "-1") String school,
                             @RequestParam(value = "academy", required = false, defaultValue = "-1") String academy,
                             @RequestParam(value = "number",required = false,defaultValue = "-1")String number,
                             @RequestParam(value = "year",required = false,defaultValue = "-1")int year,
                             @RequestParam(value = "img",required = false,defaultValue = "-1")String img) throws Exception {
        UserEntity user = userServ.getUserInRedis(token);
        UserSchool uSchool = new UserSchool(user.getuId());
        uSchool.setuSchool(school);
        uSchool.setuAcademy(academy);
        uSchool.setuNumber(number);
        uSchool.setuRegYear(year);
        uSchool.setuImg(img);
        userServ.updateUserSchool(uSchool);
        return Result.success();
    }

    @PostMapping("/daren")
    @ResponseBody
    public DataVO daren(@RequestParam(value = "token", required = true) String token,
                        @RequestParam(value = "pageNum",required = false,defaultValue = "1")int pageNum,
                        @RequestParam(value = "pageSize",required = false,defaultValue = "10")int pageSize)throws Exception{
        UserEntity user = userServ.getUserInRedis(token);
        int i = userServ.countAll();
        PageInfo pageInfo = new PageInfo(i, pageNum, pageSize);
        List<UserEntity> all = userServ.getAll(pageInfo);
        return Result.success(all,pageInfo);
    }

    @PostMapping("/darenInfo")
    @ResponseBody
    public DataVO darenInfo(@RequestParam(value = "token", required = true, defaultValue = "000") String token,
                            @RequestParam(value = "uid", required = true, defaultValue = "000") String uid) throws Exception {
        UserEntity userInRedis = userServ.getUserInRedis(token);
        DarenInfo darenInfo = userServ.darenInfo(uid);
        return Result.successToArray(darenInfo);
    }
}

package com.werun.back.controllor;

import com.werun.back.VO.DataVO;
import com.werun.back.entity.AddressEntity;
import com.werun.back.entity.ExerciseEntity;
import com.werun.back.entity.PageInfo;
import com.werun.back.entity.UserEntity;
import com.werun.back.enums.ExceptionsEnum;
import com.werun.back.exception.WeRunException;
import com.werun.back.service.AddServ;
import com.werun.back.service.ExServ;
import com.werun.back.service.UserServ;
import com.werun.back.utils.Result;
import com.werun.back.utils.StrUtils;
import com.werun.back.utils.TimeUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * @ClassName comCon
 * @Author HWG
 * @Time 2019/4/23 17:29
 */
@Api(tags = {"收货地址API"})
@Controller
@RequestMapping("/add")
@CrossOrigin
public class AddCon {
    @Autowired
    private UserServ userServ;
    @Autowired
    private AddServ addServ;
    @ApiOperation(value = "新增")
    @PostMapping("/add")
    @ResponseBody
    public DataVO insert(@RequestParam(value = "token",required = true)String token,
                         @RequestParam(value = "name",required = true)String name,
                         @RequestParam(value = "phone",required = false,defaultValue = "0")String phone,
                         @RequestParam(value = "pro",required = true)String pro,
                         @RequestParam(value = "city",required = true)String city,
                         @RequestParam(value = "county",required = true)String county,
                         @RequestParam(value = "dtl",required = true)String dtl,
                         @RequestParam(value = "default",required = false,defaultValue = "0")int def)throws Exception{
        //检查登录
        UserEntity user = userServ.getUserInRedis(token);
        //生成
        AddressEntity add=new AddressEntity();
        add.setAdId(StrUtils.timeStamp()+StrUtils.randomNum(false,6));
        add.setAdUid(user.getuId());
        add.setAdName(name);
        add.setAdProvince(pro);
        add.setAdCity(city);
        add.setAdCounty(county);
        add.setAdDtl(dtl);
        add.setAdDefault(def);
        if("0".equals(phone))
            add.setAdPhone(user.getuPhone());
        else
            add.setAdPhone(phone);
        //保存数据
        addServ.insert(add);
        if(0!=def)
            addServ.setDefault(add.getAdId(),user.getuId());
        return Result.success();
    }

    @ApiOperation(value = "默认地址")
    @PostMapping("/def")
    @ResponseBody
    public DataVO def(@RequestParam(value = "token",required = true)String token)throws Exception{
        UserEntity user = userServ.getUserInRedis(token);
        AddressEntity aDefault = addServ.getDefault(user.getuId());
        if(aDefault==null){
            throw new WeRunException(ExceptionsEnum.ADDNOADD);
        }
        return Result.success(aDefault);
    }


    @ApiOperation(value = "用户地址列表")
    @PostMapping("/list")
    @ResponseBody
    public DataVO list(@RequestParam(value = "token",required = true)String token)throws Exception{
        UserEntity user = userServ.getUserInRedis(token);
        List<AddressEntity> addList = addServ.getAddList(user.getuId());
        return Result.success(addList);
    }

    @ApiOperation(value = "设置默认地址")
    @PostMapping("/setdef")
    @ResponseBody
    public DataVO setdef(@RequestParam(value = "token",required = true)String token,@RequestParam(value = "aid",required = true)String aid)throws Exception{
        UserEntity user = userServ.getUserInRedis(token);
        int i = addServ.setDefault(aid, user.getuId());
        return Result.successToArray(i);
    }

    @ApiOperation(value = "删除地址")
    @PostMapping("/delete")
    @ResponseBody
    public DataVO delete(@RequestParam(value = "token",required = true)String token,@RequestParam(value = "aid",required = true)String aid)throws Exception{
        UserEntity user = userServ.getUserInRedis(token);
        int i = addServ.delete(aid,user.getuId());
        return Result.successToArray(i);
    }
}

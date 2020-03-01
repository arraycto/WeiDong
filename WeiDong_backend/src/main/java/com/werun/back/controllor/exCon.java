package com.werun.back.controllor;

import com.werun.back.VO.DataVO;
import com.werun.back.entity.ExerciseEntity;
import com.werun.back.entity.PageInfo;
import com.werun.back.entity.UserEntity;
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

import java.sql.Struct;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * @ClassName comCon
 * @Author HWG
 * @Time 2019/4/23 17:29
 */
@Api(tags = {"运动数据API"})
@Controller
@RequestMapping("/ex")
@CrossOrigin
public class exCon {
    @Autowired
    private UserServ userServ;
    @Autowired
    private ExServ exServ;
    @ApiOperation(value = "保存运动数据")
    @PostMapping("/add")
    @ResponseBody
    public DataVO insert(@RequestParam(value = "token",required = true)String token,
                         @RequestParam(value = "time",required = true)Long time,
                         @RequestParam(value = "amount",required = false,defaultValue = "1000")int amount,
                         @RequestParam(value = "longitude",required = false,defaultValue = "115.9448693616")String longitude,
                         @RequestParam(value = "latitude",required = false,defaultValue = "28.6886622958")String latitude)throws Exception{
        //检查登录
        UserEntity user = userServ.getUserInRedis(token);
        //生成
        ExerciseEntity ex=new ExerciseEntity();
        //amount去百度请求
        ex.setExAmount(amount);
        Date endTime = new Date();
        Date startTime=new Date(endTime.getTime()-time*1000);
        ex.setExStartTime(TimeUtil.getFormatyMdHms(startTime));
        ex.setExEndTime(TimeUtil.getFormatyMdHms(endTime));
        ex.setExAmount(Integer.parseInt(String.valueOf(time*3/2)));
        ex.setExLocationLongitude(longitude);
        ex.setExLocationLatitude(latitude);
        ex.setExId(StrUtils.timeStamp()+StrUtils.randomNum(false,5));
        ex.setuId(user.getuId());
        //保存数据
        int insert = exServ.insert(ex);
        return Result.successToArray(insert);
    }

    @ApiOperation(value = "运动数据列表")
    @PostMapping("/list")
    @ResponseBody
    public DataVO list(@RequestParam(value = "token", required = true) String token,
                       @RequestParam(value = "pageNum",required = false,defaultValue = "1")int pageNum,
                       @RequestParam(value = "pageSize",required = false,defaultValue = "10")int pageSize)throws Exception{
        UserEntity user = userServ.getUserInRedis(token);
        String formatyMdHms = TimeUtil.getFormatyMdHms();
        int count = exServ.count(user.getuId(),formatyMdHms);
        PageInfo pageInfo = new PageInfo(count, pageNum, pageSize);
        List<ExerciseEntity> select = exServ.select(user.getuId(),formatyMdHms,pageInfo);
        return Result.success(select,pageInfo);
    }


    @ApiOperation(value = "今日运动数据")
    @PostMapping("/today")
    @ResponseBody
    public DataVO list(@RequestParam(value = "token",required = true)String token)throws Exception{
        UserEntity user = userServ.getUserInRedis(token);
        Date today = TimeUtil.getDateByYMD(TimeUtil.getFormatyMd());
        Date tomorrow = new Date(today.getTime()+(long)(24*60*60*1000));
        ExerciseEntity exerciseEntity = exServ.selectOne(user.getuId(), TimeUtil.getFormatyMdHms(today),TimeUtil.getFormatyMdHms(tomorrow));
        List<ExerciseEntity> ex=new ArrayList<>();
        if(exerciseEntity!=null)
            ex.add(exerciseEntity);
        return Result.success(ex);
    }
}

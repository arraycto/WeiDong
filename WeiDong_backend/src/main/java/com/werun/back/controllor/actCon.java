package com.werun.back.controllor;

import com.werun.back.VO.DataVO;
import com.werun.back.entity.ActivityEntity;
import com.werun.back.entity.PageInfo;
import com.werun.back.entity.UserEntity;
import com.werun.back.service.ActServ;
import com.werun.back.service.UserServ;
import com.werun.back.utils.Result;
import com.werun.back.utils.StrUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.omg.PortableServer.POAManagerPackage.AdapterInactive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


/**
 * @ClassName actCon
 * @Author HWG
 * @Time 2019/4/23 8:18
 */
@Controller
@RequestMapping("/act")
@CrossOrigin
@Api(tags = {"活动API"})
public class actCon {
    @Autowired
    private UserServ userServ;
    @Autowired
    private ActServ actServ;

    @ApiOperation(value = "插入活动")
    @PostMapping("/add")
    @ResponseBody
    public DataVO insert(@RequestParam(value = "token",required = true)String token,
                         @RequestParam(value = "tid",required = true)String tid,
                         @RequestParam(value = "name",required = false,defaultValue = "新活动来啦~")String name,
                         @RequestParam(value = "desc",required = false,defaultValue = "同学们积极参与哦")String desc,
                         @RequestParam(value = "loc",required = false,defaultValue = "江西省 南昌市 青山湖区 南昌大学北区")String loc,
                         @RequestParam(value = "startTime",required = false,defaultValue = "2018-4-22 00:00:00")String stime,
                         @RequestParam(value = "endTime",required = false,defaultValue = "2018-05-02 00:00:00")String etime,
                         @RequestParam(value = "headImg",required = false,defaultValue = "default.jpg")String head)throws Exception{
        //检查登录
        UserEntity user = userServ.getUserInRedis(token);
        //生成基本信息
        ActivityEntity act=new ActivityEntity();
        act.setActId(StrUtils.timeStamp()+StrUtils.randomNum(false,5));
        act.setActUid(user.getuId());
        act.setActTid(tid);
        act.setActName(name);
        act.setActDesc(desc);
        act.setActLocation(loc);
        act.setActStartTime(stime);
        act.setActEndTime(etime);
        act.setActPreviewImg(head);
        //入库
        int insert = actServ.insert(act);

        return Result.successToArray(insert);
    }

    @ApiOperation(value = "更改状态")
    @PostMapping("/changeStatus")
    @ResponseBody
    public DataVO changeS(@RequestParam(value = "token",required = true)String token,
                          @RequestParam(value = "aid",required = true)String aid,
                          @RequestParam(value = "status",required = false,defaultValue = "2")int status)throws Exception{
        //是否登录
        UserEntity user = userServ.getUserInRedis(token);

        int i = actServ.changeActStatus(user.getuId(), aid, status);

        return Result.successToArray(i);
    }

    @ApiOperation(value = "查询活动")
    @PostMapping("/query")
    @ResponseBody
    public DataVO query(@RequestParam(value = "token",required = true)String token,
                        @RequestParam(value = "aid",required = true)String aid)throws Exception{
        //登录
        UserEntity user = userServ.getUserInRedis(token);
        List<Object> data=new ArrayList<>();
        data.add(actServ.selectByAidAndUid(aid,user));
        return Result.success(data);
    }

    @ApiOperation(value = "参加活动")
    @PostMapping("/join")
    @ResponseBody
    public DataVO join(@RequestParam(value = "token",required = true)String token,
                        @RequestParam(value = "aid",required = true)String aid)throws Exception{
        //登录
        UserEntity user = userServ.getUserInRedis(token);
        List<Object> data=new ArrayList<>();
        data.add(actServ.join(user.getuId(),aid));
        return Result.success(data);
    }

    @ApiOperation(value = "退出活动")
    @PostMapping("/out")
    @ResponseBody
    public DataVO out(@RequestParam(value = "token",required = true)String token,
                       @RequestParam(value = "aid",required = true)String aid)throws Exception{
        //登录
        UserEntity user = userServ.getUserInRedis(token);
        List<Object> data=new ArrayList<>();
        data.add(actServ.out(user.getuId(),aid));
        return Result.success(data);
    }

    @ApiOperation(value = "热门活动")
    @PostMapping("/list")
    @ResponseBody
    public DataVO list(@RequestParam(value = "token",required = true)String token,
                      @RequestParam(value = "pageNum",required =false,defaultValue = "1")int currentPage,
                       @RequestParam(value = "pageSize",required = false,defaultValue = "5")int size)throws Exception{
        //登录
        UserEntity user = userServ.getUserInRedis(token);
        //查总数
        int count = actServ.count();
        //分页信息
        PageInfo pageInfo=new PageInfo(count,currentPage,size);
        //查
        List<ActivityEntity> list = actServ.list(pageInfo);
        return Result.success(list,pageInfo);
    }

}

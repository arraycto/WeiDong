package com.werun.back.controllor;

import com.werun.back.VO.DataVO;
import com.werun.back.entity.*;
import com.werun.back.service.ActServ;
import com.werun.back.service.SRServ;
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
 * @ClassName actCon
 * @Author HWG
 * @Time 2019/4/23 8:18
 */
@Controller
@RequestMapping("/rank")
@CrossOrigin
@Api(tags = {"排名api"})
public class rankCon {
    @Autowired
    private UserServ userServ;
    @Autowired
    private SRServ srServ;

    @ApiOperation(value = "学校排名")
    @PostMapping("/schoolRank")
    @ResponseBody
    public DataVO schoolRank(@RequestParam(value = "token", required = true) String token,
                         @RequestParam(value = "pageNum", required = false, defaultValue = "1") int currentPage,
                         @RequestParam(value = "pageSize", required = false, defaultValue = "5") int size) throws Exception {
        //检查登录
        UserEntity user = userServ.getUserInRedis(token);
        String today= TimeUtil.getFormatyMd();

        int i = srServ.countToday(today);
        PageInfo pageInfo = new PageInfo(i, currentPage, size);
        List<SchoolRank> schoolRanks = srServ.selectToday(today, pageInfo);
        return Result.success(schoolRanks,pageInfo);
    }

    @ApiOperation(value = "我的排名")
    @PostMapping("/myRank")
    @ResponseBody
    public DataVO myRank(@RequestParam(value = "token", required = true) String token) throws Exception {
        //检查登录
        UserEntity user = userServ.getUserInRedis(token);
        MyRank myRank = new MyRank(192, 9, 1);
        return Result.successToArray(myRank);
    }
//
//    @ApiOperation(value = "更改状态")
//    @PostMapping("/changeStatus")
//    @ResponseBody
//    public DataVO changeS(@RequestParam(value = "token",required = true)String token,
//                          @RequestParam(value = "aid",required = true)String aid,
//                          @RequestParam(value = "status",required = false,defaultValue = "2")int status)throws Exception{
//        //是否登录
//        UserEntity user = userServ.getUserInRedis(token);
//
//        int i = actServ.changeActStatus(user.getuId(), aid, status);
//
//        return Result.successToArray(i);
//    }
//
//    @ApiOperation(value = "查询活动")
//    @PostMapping("/query")
//    @ResponseBody
//    public DataVO query(@RequestParam(value = "token",required = true)String token,
//                        @RequestParam(value = "aid",required = true)String aid)throws Exception{
//        //登录
//        UserEntity user = userServ.getUserInRedis(token);
//        List<Object> data=new ArrayList<>();
//        data.add(actServ.selectByAidAndUid(aid,user));
//        return Result.success(data);
//    }
//
//    @ApiOperation(value = "参加活动")
//    @PostMapping("/join")
//    @ResponseBody
//    public DataVO join(@RequestParam(value = "token",required = true)String token,
//                        @RequestParam(value = "aid",required = true)String aid)throws Exception{
//        //登录
//        UserEntity user = userServ.getUserInRedis(token);
//        List<Object> data=new ArrayList<>();
//        data.add(actServ.join(user.getuId(),aid));
//        return Result.success(data);
//    }
//
//    @ApiOperation(value = "退出活动")
//    @PostMapping("/out")
//    @ResponseBody
//    public DataVO out(@RequestParam(value = "token",required = true)String token,
//                       @RequestParam(value = "aid",required = true)String aid)throws Exception{
//        //登录
//        UserEntity user = userServ.getUserInRedis(token);
//        List<Object> data=new ArrayList<>();
//        data.add(actServ.out(user.getuId(),aid));
//        return Result.success(data);
//    }
//
//    @ApiOperation(value = "热门活动")
//    @PostMapping("/list")
//    @ResponseBody
//    public DataVO list(@RequestParam(value = "token",required = true)String token,
//                      @RequestParam(value = "pageNum",required =false,defaultValue = "1")int currentPage,
//                       @RequestParam(value = "pageSize",required = false,defaultValue = "5")int size)throws Exception{
//        //登录
//        UserEntity user = userServ.getUserInRedis(token);
//        //查总数
//        int count = actServ.count();
//        //分页信息
//        PageInfo pageInfo=new PageInfo(count,currentPage,size);
//        //查
//        List<ActivityEntity> list = actServ.list(pageInfo);
//        return Result.success(list,pageInfo);
//    }

}

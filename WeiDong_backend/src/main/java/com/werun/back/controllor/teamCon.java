package com.werun.back.controllor;

import com.sun.corba.se.impl.orb.ParserTable;
import com.werun.back.VO.DataVO;
import com.werun.back.VO.TeamUserVO;
import com.werun.back.entity.ActivityEntity;
import com.werun.back.entity.PageInfo;
import com.werun.back.entity.TeamEntity;
import com.werun.back.entity.UserEntity;
import com.werun.back.service.ActServ;
import com.werun.back.service.TeamServ;
import com.werun.back.service.UserServ;
import com.werun.back.utils.Result;
import com.werun.back.utils.StrUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


/**
 * @ClassName actCon
 * @Author HWG
 * @Time 2019/4/23 8:18
 */
@Controller
@RequestMapping("/team")
@CrossOrigin
@Api(tags = {"小组API"})
public class teamCon {
    @Autowired
    private UserServ userServ;
    @Autowired
    private TeamServ teamServ;

    @ApiOperation(value = "建立小组")
    @PostMapping("/add")
    @ResponseBody
    public DataVO insert(@RequestParam(value = "token",required = true)String token,
                         @RequestParam(value = "name",required = false,defaultValue = "萌新小组")String name,
                         @RequestParam(value = "desc",required = false,defaultValue = "本小组刚刚出世不久哦")String desc,
                         @RequestParam(value = "loc",required = false,defaultValue = "南昌大学")String loc,
                         @RequestParam(value = "headImg",required = false,defaultValue = "default.jpg")String head)throws Exception{
        //检查登录
        UserEntity user = userServ.getUserInRedis(token);
        //生成基本信息
        TeamEntity team=new TeamEntity();
        team.setTeamId(StrUtils.timeStamp()+StrUtils.randomNum(false,5));
        team.setTeamUid(user.getuId());
        team.setTeamName(name);
        team.setTeamDesc(desc);
        team.setTeamLocation(loc);
        team.setActAvatar(head);

        //入库
        int insert = teamServ.insert(team);

        return Result.successToArray(insert);
    }

    @ApiOperation(value = "更改状态")
    @PostMapping("/changeStatus")
    @ResponseBody
    public DataVO changeS(@RequestParam(value = "token",required = true)String token,
                          @RequestParam(value = "tid",required = true)String tid,
                          @RequestParam(value = "status",required = false,defaultValue = "2")int status)throws Exception{
        //是否登录
        UserEntity user = userServ.getUserInRedis(token);

        int i = teamServ.changeTeamStatus(user.getuId(), tid, status);

        return Result.successToArray(i);
    }

    @ApiOperation(value = "小组详情")
    @PostMapping("/query")
    @ResponseBody
    public DataVO query(@RequestParam(value = "token",required = true)String token,
                        @RequestParam(value = "tid",required = true)String tid)throws Exception{
        //登录
        UserEntity user = userServ.getUserInRedis(token);
        TeamUserVO teamUserVO = teamServ.selectByTidAndUid(tid, user);
        return Result.successToArray(teamUserVO);
    }

    @ApiOperation(value = "加入小组")
    @PostMapping("/join")
    @ResponseBody
    public DataVO join(@RequestParam(value = "token",required = true)String token,
                        @RequestParam(value = "tid",required = true)String tid)throws Exception{
        //登录
        UserEntity user = userServ.getUserInRedis(token);
        int join = teamServ.join(user.getuId(), tid);
        return Result.successToArray(join);
    }

    @ApiOperation(value = "退出小组")
    @PostMapping("/out")
    @ResponseBody
    public DataVO out(@RequestParam(value = "token",required = true)String token,
                       @RequestParam(value = "tid",required = true)String tid)throws Exception{
        //登录
        UserEntity user = userServ.getUserInRedis(token);
        int out = teamServ.out(user.getuId(), tid);
        return Result.successToArray(out);
    }

    @ApiOperation(value = "小组推荐")
    @PostMapping("/list")
    @ResponseBody
    public DataVO list(@RequestParam(value = "token",required = true)String token,
                      @RequestParam(value = "pageNum",required =false,defaultValue = "1")int currentPage,
                       @RequestParam(value = "pageSize",required = false,defaultValue = "5")int size)throws Exception{
        //登录
        UserEntity user = userServ.getUserInRedis(token);
        //查总数
        int count = teamServ.count();
        //分页信息
        PageInfo pageInfo=new PageInfo(count,currentPage,size);
        //查
        List<TeamEntity> list = teamServ.list(pageInfo);
        return Result.success(list,pageInfo);
    }

    @ApiOperation(value = "搜索")
    @PostMapping("/search")
    @ResponseBody
    public DataVO search(@RequestParam(value = "token",required = true)String token,
                         @RequestParam(value = "key",required = true,defaultValue = "跑步")String key,
                       @RequestParam(value = "pageNum",required =false,defaultValue = "1")int currentPage,
                       @RequestParam(value = "pageSize",required = false,defaultValue = "5")int size)throws Exception{
        //登录
        UserEntity user = userServ.getUserInRedis(token);
        //查总数
        int count = teamServ.count(key);
        //分页信息
        PageInfo pageInfo=new PageInfo(count,currentPage,size);
        //查
        List<TeamEntity> list = teamServ.search(key,pageInfo);
        return Result.success(list,pageInfo);
    }

}

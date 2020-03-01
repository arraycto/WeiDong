package com.werun.back.controllor;

import com.werun.back.VO.ComUserVO;
import com.werun.back.VO.DataVO;
import com.werun.back.entity.CommentEntity;
import com.werun.back.entity.PageInfo;
import com.werun.back.entity.UserEntity;
import com.werun.back.service.AnnexServ;
import com.werun.back.service.ComServ;
import com.werun.back.service.DiaryServ;
import com.werun.back.service.UserServ;
import com.werun.back.utils.Result;
import com.werun.back.utils.StrUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * @ClassName comCon
 * @Author HWG
 * @Time 2019/4/23 17:29
 */
@Api(tags = {"评论API"})
@Controller
@RequestMapping("/comment")
@CrossOrigin
public class comCon {
    @Autowired
    private UserServ userServ;
    @Autowired
    private ComServ comServ;
    @Autowired
    private DiaryServ diaryServ;
    @Autowired
    private AnnexServ annexServ;
    @ApiOperation(value = "添加新评论")
    @PostMapping("/add")
    @ResponseBody
    public DataVO insert(@RequestParam(value = "token",required = true)String token,
                         @RequestParam(value = "did",required = true)String did,
                         @RequestParam(value = "content",required = true)String content,
                         @RequestParam(value = "type",required = false,defaultValue = "1")int type,
                         @RequestParam(value = "buid",required = false,defaultValue = "")String buid,
                         @RequestParam(value = "imgs",required = false,defaultValue = ",")String imgs)throws Exception{
        //检查登录
        UserEntity user = userServ.getUserInRedis(token);
        //生成
        CommentEntity com=new CommentEntity();
        com.setComId(StrUtils.timeStamp()+StrUtils.randomNum(false,5));
        com.setComItemId(did);
        com.setComUid(user.getuId());
        com.setComBUid(buid);
        com.setComType(type);
        com.setComContent(content);
        //插入
        int insert = comServ.insert(com);
        int comment = diaryServ.comment(did);
        annexServ.insert(imgs,com.getComId());
        return Result.successToArray(insert);
    }

    @ApiOperation(value = "查评论列表")
    @PostMapping("/list")
    @ResponseBody
    public DataVO list(@RequestParam(value = "token",required = true)String token,
                       @RequestParam(value = "did",required = true)String did,
                       @RequestParam(value = "pageNum",required = false,defaultValue = "1")int pageNum,
                       @RequestParam(value = "pageSize",required = false,defaultValue = "10")int pageSize)throws Exception{
        UserEntity user = userServ.getUserInRedis(token);
        int count = comServ.count(did);
        PageInfo pageInfo=new PageInfo(count,pageNum,pageSize);
        List<ComUserVO> comUserVOS = comServ.selectUserByDid(did,user.getuId(), pageInfo);
        return Result.success(comUserVOS,pageInfo);
    }

}

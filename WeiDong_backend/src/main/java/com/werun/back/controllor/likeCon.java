package com.werun.back.controllor;

import com.sun.org.apache.regexp.internal.RE;
import com.werun.back.VO.DataVO;
import com.werun.back.entity.LikeEntity;
import com.werun.back.entity.PageInfo;
import com.werun.back.entity.UserEntity;
import com.werun.back.service.ComServ;
import com.werun.back.service.DiaryServ;
import com.werun.back.service.LikeServ;
import com.werun.back.service.UserServ;
import com.werun.back.utils.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @ClassName likeCon
 * @Author HWG
 * @Time 2019/4/23 15:47
 */
@Api(tags = "点赞API")
@Controller
@RequestMapping("/like")
@CrossOrigin
public class likeCon {
    @Autowired
    private LikeServ likeServ;
    @Autowired
    private DiaryServ diaryServ;
    @Autowired
    private UserServ userServ;
    @Autowired
    private ComServ comServ;

    @ApiOperation(value = "新增点赞")
    @PostMapping("/add")
    @ResponseBody
    public DataVO like(@RequestParam(value = "token", required = true) String token,
                       @RequestParam(value = "id", required = true) String did,
                       @RequestParam(value = "type", required = false, defaultValue = "1") int type) throws Exception {
        //检查登录
        UserEntity user = userServ.getUserInRedis(token);
        //生成点赞
        int i = likeServ.reLike(user.getuId(), did, 1);
        if (type == 1) {
            if (i == 2) {
                diaryServ.cancleLike(did);
            } else {
                diaryServ.like(did);
            }
        }else if(type==2){
            if (i == 2) {
                comServ.cancleLike(did);
            } else {
                comServ.like(did);
            }
        }
        return Result.successToArray(i);
    }

    @ApiOperation(value = "点赞列表")
    @PostMapping("/list")
    @ResponseBody
    public DataVO list(@RequestParam(value = "token", required = true) String token,
                       @RequestParam(value = "id", required = true) String did,
                       @RequestParam(value = "pageNum", required = false, defaultValue = "1") int pageNum,
                       @RequestParam(value = "pageSize", required = false, defaultValue = "20") int pageSize) throws Exception {
        //检查登录
        UserEntity user = userServ.getUserInRedis(token);
        //数量
        int count = likeServ.count(did, 1, 1);
        //分页信息
        PageInfo pageInfo = new PageInfo(count, pageNum, pageSize);
        //查询
        List<UserEntity> likeEntities = likeServ.selectUserList(did, 1, 1, pageInfo);
        return Result.success(likeEntities, pageInfo);
    }

}

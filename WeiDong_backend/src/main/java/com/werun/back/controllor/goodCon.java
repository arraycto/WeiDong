package com.werun.back.controllor;

import com.werun.back.VO.DataVO;
import com.werun.back.VO.GoodUserAnnexVO;
import com.werun.back.VO.GoodsUserEntity;
import com.werun.back.entity.GoodsEntity;
import com.werun.back.entity.PageInfo;
import com.werun.back.entity.UserEntity;
import com.werun.back.service.AnnexServ;
import com.werun.back.service.GoodsServ;
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
 * @ClassName goodCon
 * @Author HWG
 * @Time 2019/4/20 8:45
 */
@Api(tags = {"商品CRDU 接口"})
@RequestMapping("/good")
@CrossOrigin
@Controller
public class goodCon {
    @Autowired
    private GoodsServ goodsServ;
    @Autowired
    private UserServ userServ;
    @Autowired AnnexServ annexServ;
    @ApiOperation(value = "入库商品")
    @PostMapping("/insert")
    @ResponseBody
    public DataVO insert(@RequestParam(value = "token",required = true)String token,
                         @RequestParam(value = "price",required = true)int price ,
                         @RequestParam(value = "oldprice",required = true)int oldprice ,
                         @RequestParam(value = "name",required = false,defaultValue = "官方自营")String name,
                         @RequestParam(value = "previewImg",required = false,defaultValue = "default10001.jpg")String previewImg ,
                         @RequestParam(value = "desc",required = false,defaultValue = "无")String desc ,
                         @RequestParam(value = "stock",required = false,defaultValue = "1")int stock,
                         @RequestParam(value = "type",required = false,defaultValue= "10001")int type,
                         @RequestParam(value = "annex",required = false,defaultValue = "")String annex)throws Exception{
        //检查是否登录
        UserEntity user = userServ.getUserInRedis(token);
        //插入商品基本信息
        GoodsEntity g=new GoodsEntity();
        g.setgId(StrUtils.timeStamp()+ StrUtils.randomNum(false,4));
        g.setgUid(user.getuId());
        g.setgPrice(price);
        g.setgOldprice(oldprice);
        g.setgName(name);
        g.setgPreviewImg(previewImg);
        g.setgDesc(desc);
        g.setgStock(stock);
        g.setgType(type);
        int insert = goodsServ.insert(g);
        //插入商品附件
        int insert1 = annexServ.insert(annex, g.getgId());
        return Result.success("新增商品:"+insert+"\n商品附件:"+insert1);
    }

    @ApiOperation(value = "商品详细信息",notes = "token一定要有，gid一定要有。")
    @PostMapping("/goodInfo")
    @ResponseBody
    public DataVO goodInfo(@RequestParam(value = "token",required = true,defaultValue = "000")String token,
                           @RequestParam(value = "gid",required = true,defaultValue = "000")String gid)throws Exception{
        UserEntity user = userServ.getUserInRedis(token);
        //查商品基本信息
        GoodUserAnnexVO goodInfo = goodsServ.selectGoodInfoByGid(gid);

        return Result.success(goodInfo);
    }

    @ApiOperation(value = "商品列表",notes = "token必须要有，返回结果会带有分页信息")
    @PostMapping("/goodList")
    @ResponseBody
    public DataVO goodList(@RequestParam(value = "token",required = true,defaultValue = "000")String token,
                           @RequestParam(value = "goodType",required = false,defaultValue = "1001")int goodType,
                           @RequestParam(value = "goodStatus",required = false,defaultValue = "1")int goodStatus,
                           @RequestParam(value = "pageNum",required = false,defaultValue = "1")int pageNum,
                           @RequestParam(value = "pageSize",required = false,defaultValue = "5")int pageSize)throws Exception{
        //检查是否已登录
        UserEntity user = userServ.getUserInRedis(token);
        //获取商品总数量
        int goodNum = goodsServ.getGoodNum(goodType, goodStatus);
        //生成分页信息
        PageInfo page=new PageInfo(goodNum,pageNum,pageSize);
        //获取某一页商品
        List<GoodsUserEntity> goods = goodsServ.selectGoodByType(goodType, goodStatus, page);
        return Result.success(goods,page);
    }

    @ApiOperation(value = "改变状态",notes = "这个仅仅用于，用户撤回自己发布的出租")
    @PostMapping("/cancle")
    @ResponseBody
    public DataVO cancle(@RequestParam(value = "token",required = true)String token,
                         @RequestParam(value = "gid",required = true)String gid,
                         @RequestParam(value = "status",required = false,defaultValue = "3")int status)throws Exception{
        //检查是否已登录
        UserEntity user = userServ.getUserInRedis(token);
        //改变状态
        int i = goodsServ.changeStatus(gid, status, user.getuId());

        return Result.success(i);
    }

}

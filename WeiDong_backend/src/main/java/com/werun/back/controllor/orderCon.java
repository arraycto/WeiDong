package com.werun.back.controllor;

import com.werun.back.VO.DataVO;
import com.werun.back.VO.OrderGoodVO;
import com.werun.back.entity.GoodsEntity;
import com.werun.back.entity.OrderEntity;
import com.werun.back.entity.PageInfo;
import com.werun.back.entity.UserEntity;
import com.werun.back.service.AnnexServ;
import com.werun.back.service.GoodsServ;
import com.werun.back.service.OrderServ;
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
@Api(value = "订单操作API",tags = {"订单CRDU 接口"})
@RequestMapping("/order")
@CrossOrigin
@Controller
public class orderCon {
    @Autowired
    private GoodsServ goodsServ;
    @Autowired
    private UserServ userServ;
    @Autowired
    private OrderServ orderServ;

    @ApiOperation(value = "生成新订单")
    @PostMapping("/new")
    @ResponseBody
    public DataVO insert(@RequestParam(value = "token",required = true)String token,
                         @RequestParam(value = "gid",required = true)String gid,
                         @RequestParam(value = "num",required = false,defaultValue = "1")int num,
                         @RequestParam(value = "address",required = true)String address)throws Exception{
        //检查是否登录
        UserEntity user = userServ.getUserInRedis(token);
        //生成订单基本信息
        GoodsEntity goods = goodsServ.selectGoodByGid(gid);
        OrderEntity order=new OrderEntity();
        order.setoId(StrUtils.timeStamp()+StrUtils.randomNum(false,5));
        order.setoUid(user.getuId());
        order.setoGid(gid);
        order.setoNum(num);
        order.setoPrice(num*goods.getgPrice());
        order.setoAddress(address);
        //先更新库存
        goodsServ.buy(gid, num);
        //保存订单信息
        int insert = orderServ.insert(order);
        return Result.success(insert);
    }

    @ApiOperation(value = "订单详情")
    @PostMapping("/orderInfo")
    @ResponseBody
    public DataVO orderInfo(@RequestParam(value = "token",required = true)String token,
                            @RequestParam(value = "oid",required = true)String oid)throws Exception{
        //检查是否登录
        UserEntity user = userServ.getUserInRedis(token);
        //查询订单 order&goods
        OrderGoodVO orderGoodVO = orderServ.selectOrderInfo(oid);
        return Result.success(orderGoodVO);
    }

    @ApiOperation(value = "获取订单列表")
    @PostMapping("/orderList")
    @ResponseBody
    public DataVO orderList(@RequestParam(value = "token",required = true)String token,
                            @RequestParam(value = "pageNum",required = false,defaultValue = "1")int pageNum,
                            @RequestParam(value = "pageSize",required = false,defaultValue = "5")int pageSize,
                            @RequestParam(value = "fromStatus",required = false,defaultValue = "1")int fromStatus,
                            @RequestParam(value = "endStatus",required = false,defaultValue = "5")int endStatus)throws Exception{
        //检查是否登录
        UserEntity user = userServ.getUserInRedis(token);
        //获取分页信息
        int count = orderServ.count(user.getuId(), fromStatus, endStatus);
        //生成分页信息
        PageInfo pageInfo=new PageInfo(count,pageNum,pageSize);
        //获取商品列表
        List<OrderGoodVO> orderGoodVOS = orderServ.selectOrderList(user.getuId(), fromStatus, endStatus, pageInfo);


        return Result.success(orderGoodVOS,pageInfo);
    }

    @ApiOperation(value = "改变订单状态")
    @PostMapping("/changeOrder")
    @ResponseBody
    public DataVO changeOrder(@RequestParam(value = "token",required = true)String token,
                            @RequestParam(value = "oid",required = true)String oid,
                              @RequestParam(value = "status",required = false,defaultValue = "-1")int status)throws Exception{
        //检查是否登录
        UserEntity user = userServ.getUserInRedis(token);
        //改变订单状态
        int i = orderServ.changeStatus(oid, status, user.getuId());
        return Result.success(i);
    }

}

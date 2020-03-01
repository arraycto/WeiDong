package com.werun.back.service;

import com.werun.back.VO.OrderGoodVO;
import com.werun.back.dao.OrderDao;
import com.werun.back.entity.GoodsEntity;
import com.werun.back.entity.OrderEntity;
import com.werun.back.entity.PageInfo;
import com.werun.back.enums.ExceptionsEnum;
import com.werun.back.exception.WeRunException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName OrderServ
 * @Author HWG
 * @Time 2019/4/20 16:41
 */
@Service
public class OrderServ {
    @Resource
    private OrderDao orderDao;
    @Resource
    private GoodsServ goodsServ;

    //插入新订单
    public int insert(OrderEntity o)throws Exception{
        int re;
        try {
            re=orderDao.insert(o);
        }catch (Exception e){
            e.printStackTrace();
            throw new WeRunException(ExceptionsEnum.INSERTORDERFAILED);
        }
        return re;
    }
    //查询一个订单  得到订单实体类
    public OrderEntity selectByOid(String oid)throws Exception{
        OrderEntity o;
        try {
            o= orderDao.selectByOid(oid);
        }catch (Exception e){
            e.printStackTrace();
            throw new WeRunException(ExceptionsEnum.NODATA);
        }
        return o;
    }
    //查询一个订单信息  orderGoods
    public OrderGoodVO selectOrderInfo(String oid)throws Exception{
        OrderGoodVO ogv;
        OrderEntity order = selectByOid(oid);
        GoodsEntity good;
        try {
            good=goodsServ.selectGoodByGid(order.getoGid());
            ogv=new OrderGoodVO(order);
            ogv.setGood(good);
        }catch (Exception e){
            e.printStackTrace();
            throw new WeRunException(ExceptionsEnum.NOGOODDATA);
        }
        return ogv;
    }
    //用户订单的总数量，方便分页
    public int count(String uid,int fromStatus,int endStatus)throws Exception{
        int count;
        try {
            count=orderDao.count(uid,fromStatus,endStatus);
        }catch (Exception e){
            e.printStackTrace();
            throw new WeRunException(ExceptionsEnum.NODATA);
        }
        return count;
    }
    //符合条件的订单列表
    public List<OrderEntity> selectByUidStatus(String uid, int fromStatus, int endStatus, PageInfo pageInfo)throws Exception{
        List<OrderEntity> orders =new ArrayList<>();
        try {
            orders=orderDao.selectByUidStatus(uid,fromStatus,endStatus,pageInfo.getFromIndex(),pageInfo.getPageSize());
        }catch (Exception e){
            e.printStackTrace();
            throw new WeRunException(ExceptionsEnum.NODATA);
        }
        return orders;

    }
    //订单列表  order+good
    public List<OrderGoodVO> selectOrderList(String uid, int fromStatus, int endStatus, PageInfo pageInfo)throws Exception{

        List<OrderEntity> orders = selectByUidStatus(uid, fromStatus, endStatus, pageInfo);
        List<OrderGoodVO> ogv=new ArrayList<>();
        try {
            for (OrderEntity o:orders) {
                OrderGoodVO og=new OrderGoodVO(o);
                og.setGood(goodsServ.selectGoodByGid(o.getoGid()));
                ogv.add(og);
            }
        }catch (Exception e){
            e.printStackTrace();
            throw new WeRunException(ExceptionsEnum.NOGOODDATA);
        }

        return ogv;
    }
    //改变订单状态
    public int changeStatus(String oid,int status,String uid)throws Exception{
        int result;
        OrderEntity orderEntity = selectByOid(oid);
        if(orderEntity==null)
            throw new WeRunException(ExceptionsEnum.INVALIDORDER);
        else if(!uid.equals(orderEntity.getoUid()))
            throw new WeRunException(ExceptionsEnum.NOAUTHORITY);
        try {
            result =orderDao.updateOrder(oid,status);
        }catch (Exception e){
            e.printStackTrace();
            throw new WeRunException(ExceptionsEnum.ORDERUPDATEFAILED);
        }
        return result;

    }
}

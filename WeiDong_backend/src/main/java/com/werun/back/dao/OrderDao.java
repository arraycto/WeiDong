package com.werun.back.dao;

import com.werun.back.entity.OrderEntity;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * @ClassName OrderDao
 * @Author HWG
 * @Time 2019/4/20 16:14
 */
public interface OrderDao {
    //插入订单
    @Insert("insert into orders(o_id,o_uid,o_gid,o_price,o_num,o_address) " +
            "values(#{oId},#{oUid},#{oGid},#{oPrice},#{oNum},#{oAddress});")
    int insert(OrderEntity o);

    //查询订单 by oid
    @Select("select o_id,o_uid,o_gid,o_price,o_num,o_time,o_status,o_address " +
            "from orders where o_id=#{1};")
    OrderEntity selectByOid(@Param("1")String oid);

    //查询符合条件的订单总数
    @Select("select count(*) " +
            "from orders where o_uid=#{1} and (o_status between #{2} and #{3});")
    int count(@Param("1")String uid, @Param("2")int fromStatus, @Param("3")int endStatus);

    //查询订单 by uid&status
    @Select("select o_id,o_uid,o_gid,o_price,o_num,o_time,o_status,o_address " +
            "from orders where o_uid=#{1} and (o_status between #{2} and #{3}) order by o_time desc limit #{4},#{5};")
    List<OrderEntity> selectByUidStatus(@Param("1")String uid, @Param("2")int fromStatus, @Param("3")int endStatus, @Param("4")int fromIndex, @Param("5")int pageSize);

    //改变订单状态
    @Update("update orders set o_status=#{2} where o_uid=#{1};")
    int updateOrder(@Param("1")String oid,@Param("2")int status);
}

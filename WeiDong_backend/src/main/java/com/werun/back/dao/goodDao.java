package com.werun.back.dao;

import com.werun.back.entity.GoodsEntity;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

/**
 * @ClassName goodDao
 * @Author HWG
 * @Time 2019/4/20 8:00
 */

public interface goodDao {

    //插入一个商品
    @Insert("insert into goods(g_id,g_uid,g_price,g_oldprice,g_name,g_preview_img,g_desc,g_stock,g_type) " +
            "values(#{gId},#{gUid},#{gPrice},#{gOldprice},#{gName},#{gPreviewImg},#{gDesc},#{gStock},#{gType});")
    int insert(GoodsEntity g);

    //查询一个商品
    @Select("select g_id,g_uid,g_price,g_oldprice,g_name,g_preview_img,g_desc,g_stock,g_sale_num,g_type,g_status " +
            "from goods where g_id=#{1};")
    GoodsEntity selectByGid(@Param("1")String gid);

    //获取商品列表
    @Select("select g_id,g_uid,g_price,g_oldprice,g_name,g_preview_img,g_desc,g_stock,g_sale_num,g_type " +
            "from goods where g_type=#{1} and g_status=#{2} order by g_sale_num,g_time desc limit #{3},#{4};")
    List<GoodsEntity> selectByType(@Param("1")int type,
                                   @Param("2")int status,
                                   @Param("3")int fromIndex,
                                   @Param("4")int size);

    //获取商品数量
    @Select("select count(*) from goods where g_type=#{1} and g_status=#{2} ;")
    int selectGoodNum(@Param("1")int type,@Param("2") int status);

    //购买
    @Update("update goods set g_sale_num=g_sale_num+1,g_stock=g_stock-#{2} where g_id=#{1};")
    int buyGood(@Param("1")String gid,@Param("2")int num);

    //下架
    @Update("update goods set g_status=#{2} where g_id=#{1}; ")
    int changeStatus(@Param("1")String gid,@Param("2")int status);

}

package com.werun.back.dao;

import com.werun.back.entity.LikeEntity;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface likeDao {

    //查询一个点赞
    @Select("select " +
            "like_item_id,like_uid,like_type,like_status " +
            "from likes where " +
            "like_item_id=#{1} and like_uid=#{2};")
    LikeEntity queryLikeByUid(@Param(value = "1") String lid, @Param(value = "2") String uid);

    //批量查询点赞
    @Select("select " +
            "like_item_id,like_uid " +
            "from likes where " +
            "like_status=#{3} and like_item_id=#{1} and like_type=#{2} order by like_time desc limit #{4},#{5};")
    List<LikeEntity> queryByLid(@Param(value = "1") String lid, @Param("2") int type, @Param("3") int status, @Param("4") int fromIndex, @Param("5") int size);

    //查询点赞数
    @Select("select " +
            "count(*) " +
            "from likes where " +
            "like_status=#{3} and like_item_id=#{1} and like_type=#{2}")
    int count(@Param(value = "1") String lid, @Param(value = "2") int type, @Param("3") int status);


    //插入一个点赞
    @Insert("insert into likes" +
            "(like_item_id,like_uid,like_type) " +
            "values" +
            "(#{likeItemId},#{likeUid},#{likeType});")
    int insert(LikeEntity i);

    //取消点赞或 点赞
    @Update("update likes set like_status = #{3} where like_item_id=#{2} and like_uid=#{1};")
    int relike(@Param(value = "1") String uid, @Param(value = "2") String lid, @Param("3") int status);

}

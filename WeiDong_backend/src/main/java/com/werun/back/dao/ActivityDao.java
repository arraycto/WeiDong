package com.werun.back.dao;

import com.werun.back.entity.ActivityEntity;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * @ClassName ActivityDao
 * @Author HWG
 * @Time 2019/4/22 16:13
 */
public interface ActivityDao {
    //插入一个活动
    @Insert("insert into activity" +
            "(act_id,act_uid,act_tid,act_name,act_desc,act_location,act_start_time,act_end_time,act_preview_img) " +
            "values" +
            "(#{actId},#{actUid},#{actTid},#{actName},#{actDesc},#{actLocation},#{actStartTime},#{actEndTime},#{actPreviewImg});")
    int insert(ActivityEntity act);

    //改变状态
    @Update("update activity set act_status=#{2} where act_id=#{1};")
    int changeActStatus(@Param("1")String aid,@Param("2")int status);

    //根据aid查询
    @Select("select " +
            "act_id,act_uid,act_tid,act_name,act_desc,act_location,act_start_time,act_end_time,act_user_num,act_preview_img,act_status " +
            "from activity where act_id=#{1};")
    ActivityEntity selectByAid(@Param("1")String aid);

    //根据时间查询
    @Select("select " +
            "act_id,act_uid,act_tid,act_name,act_desc,act_location,act_start_time,act_user_num,act_end_time,act_preview_img " +
            "from activity " +
            "where act_status=1 and (act_end_time > current_timestamp() ) " +
            "order by act_user_num desc,act_set_time desc " +
            "limit #{1},#{2};")
    List<ActivityEntity> selectByTime(@Param("1")int fromIndex,@Param("2")int size);

    //查询符合要求的coutn
    @Select("select count(*) from activity where act_status=1 and (act_end_time > current_timestamp() ) ;")
    int count();

    //报名活动
    @Update("update activity set act_user_num=act_user_num+1 where act_id=#{1};")
    int join(@Param("1")String aid);
    @Insert("insert into user2team" +
            "(t_id,u_id,type) " +
            "values" +
            "(#{1},#{2},1);")
    int insertIntoUser2Team(@Param("1")String aid,@Param("2")String uid);
    @Update("update user2team set status=1 where t_id=#{1} and u_id=#{2};")
    int reJoin(@Param("1")String id,@Param("2")String uid);
    //退出活动
    @Update("update activity set act_user_num=act_user_num-1 where act_id=#{1};")
    int logout(@Param("1")String aid);
    @Update("update user2team set status=2 where t_id=#{1} and u_id=#{2};")
    int logoutTeam(@Param("1")String id,@Param("2")String uid);

    //查询报名情况
    @Select("select status from user2team where t_id=#{1} and u_id=#{2};")
    Object queryRepository(@Param("1") String aid,@Param("2") String uid);
}

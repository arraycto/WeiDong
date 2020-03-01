package com.werun.back.dao;

import com.werun.back.entity.TeamEntity;
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
public interface TeamDao {
    //插入一个小组
    @Insert("insert into team " +
            "(team_id,team_uid,team_name,team_desc,team_location,act_avatar) " +
            "values" +
            "(#{teamId},#{teamUid},#{teamName},#{teamDesc},#{teamLocation},#{actAvatar});")
    int insert(TeamEntity team);

    //查询一个小组 通过tid
    @Select("select team_id,team_uid,team_name,team_desc,team_location,team_time,act_user_num,act_avatar " +
            "from team " +
            "where team_id=#{1};")
    TeamEntity selectByTid(@Param("1")String tid,@Param("2")int status);


    //改变小组状态
    @Update("update team set team_status=#{2} " +
            "where team_id=#{1};")
    int changeStatus(@Param("1")String tid,@Param("2")int status);

    //加入小组
    @Update("update team set act_user_num=act_user_num+1 " +
            "where team_id=#{1};")
    int join(@Param("1")String tid);
    @Insert("insert into user2team(t_id,u_id,type) values(#{1},#{2},2)")
    int uJoin(@Param("1")String tid,@Param("2")String uid);

    //重新加入
    @Update("update user2team set status=1 where t_id=#{1} and u_id=#{2};")
    int reJoin(@Param("1")String tid,@Param("2")String uid);

    //退出小组
    @Update("update team set act_user_num=act_user_num-1 " +
            "where team_id=#{1};")
    int out(@Param("1")String tid);
    @Update("update user2team set status=2 where t_id=#{1} and u_id=#{2};")
    int logoutTeam(@Param("1")String tid,@Param("2")String uid);

    //查询符合要求的coutn
    @Select("select count(*) from team where team_status=1 ;")
    int count();

    //查询符合要求的coutn
    @Select("select count(*) from team where team_status=1 and team_name like #{1};")
    int countSearch(@Param("1")String likeName);

    //查询很多小组  pageinfo==推荐
    @Select("select team_id,team_uid,team_name,team_desc,team_location,team_time,act_user_num,act_avatar " +
            "from team " +
            "where team_status=#{1} order by team_time desc limit #{2},#{3};")
    List<TeamEntity> selectByHot(@Param("1")int status,@Param("2")int fromIndex,@Param("3")int size);

    //查询很多小组  pageinfo==推荐
    @Select("select team_id,team_uid,team_name,team_desc,team_location,team_time,act_user_num,act_avatar " +
            "from team " +
            "where team_status=#{1} order by team_time desc limit #{2},#{3};")
    List<TeamEntity> selectByTime(@Param("1")int status,@Param("2")int fromIndex,@Param("3")int size);

    //查询
    @Select("select team_id,team_uid,team_name,team_desc,team_location,team_time,act_user_num,act_avatar " +
            "from team " +
            "where team_status=#{1} and team_name like #{2} order by team_time desc limit #{3},#{4};")
    List<TeamEntity> selectByName(@Param("1")int status,@Param("2")String likeName,@Param("3")int fromIndex,@Param("4")int size);

    //查询小组成员列表
    @Select("select u_id from user2team where t_id=#{1} and status=#{2}")
    List<String> selectUser(@Param("1")String tid,@Param("2")int status);

    //查询报名情况
    @Select("select status from user2team where t_id=#{1} and u_id=#{2};")
    Object queryRepository(@Param("1") String tid,@Param("2") String uid);
}

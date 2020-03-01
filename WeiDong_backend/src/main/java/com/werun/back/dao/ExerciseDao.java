package com.werun.back.dao;

import com.werun.back.entity.ExerciseEntity;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

/**
 * @ClassName ExerciseDao
 * @Author HWG
 * @Time 2019/4/26 14:55
 */
public interface ExerciseDao {
    //插入运动数据
    @Insert("insert into exercise" +
            "(ex_id,u_id,ex_amount,ex_location_latitude,ex_location_longitude,ex_start_time,ex_end_time) " +
            "values" +
            "(#{exId},#{uId},#{exAmount},#{exLocationLatitude},#{exLocationLongitude},#{exStartTime},#{exEndTime});")
    int insert(ExerciseEntity ex);

    //查询一天的数据
    @Select("select sum(ex_amount) as ex_amount,u_id " +
            "from exercise " +
            "where u_id=#{1} and ex_start_time between #{2} and #{3};")
    ExerciseEntity select(@Param("1")String uid,@Param("2")String sTime,@Param("3")String eTime);

    //查询是否有数据
    @Select("select count(*) from exercise where u_id=#{1} and ex_start_time < #{2} limit 2;")
    int checkNum(@Param("1")String uid,@Param("2")String t);

    //查询最近的一条数据
    @Select("select " +
            "ex_id,u_id,ex_amount,ex_location_latitude,ex_location_longitude,ex_start_time,ex_end_time " +
            "from exercise " +
            "where u_id=#{1} and ex_start_time < #{2} order by ex_start_time desc limit 0,1;")
    ExerciseEntity selectLastest(@Param("1")String uid,@Param("2")String t);

    //查询数据
    @Select("select " +
            "ex_amount,ex_location_latitude,ex_location_longitude,ex_start_time,ex_end_time " +
            " from exercise where u_id =#{4} and ex_end_time < #{1} order by ex_start_time desc limit #{2},#{3};")
    List<ExerciseEntity> selectDtl(@Param("1")String endTime,@Param("2")int fromInex,@Param("3")int size,@Param("4")String uid);
    //查数量
    @Select("select " +
            " count(*) " +
            " from  exercise where  u_id =#{2} and ex_end_time < #{1} ;")
    int count(@Param("1")String endTime,@Param("2")String uid);


}

package com.werun.back.dao;

import com.werun.back.entity.SchoolRank;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @ClassName SRDao
 * @Author HWG
 * @Time 2019/5/2 9:41
 */
public interface SRDao {
    @Select("select s_name,s_stu_num,s_ex_time,s_history_num from " +
            "school_rank where s_update_time > #{1} order by s_ex_time desc limit #{2},#{3};")
    List<SchoolRank> selectToday(@Param("1")String time,@Param("2")int fromIndex,@Param("3")int size);

    @Select("select count(*) from " +
            "school_rank where s_update_time > #{1};")
    int countToday(@Param("1")String time);
}

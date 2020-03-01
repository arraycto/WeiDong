package com.werun.back.dao;

import com.werun.back.entity.AnnexEntity;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @ClassName AnnexDao
 * @Author HWG
 * @Time 2019/4/20 10:01
 */
public interface AnnexDao {

    //插入一个附件
    @Insert("insert into annex(post_id,annex_name,annex_order) " +
            "values(#{postId},#{annexName},#{annexOrder});")
    int insert(AnnexEntity a);

    //查询附件
    @Select("select annex_name from annex where post_id=#{1} order by annex_order limit 9;")
    List<AnnexEntity> selectByPid(@Param("1") String pid);
}

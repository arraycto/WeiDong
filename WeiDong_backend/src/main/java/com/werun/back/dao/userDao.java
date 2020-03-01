package com.werun.back.dao;

import com.werun.back.entity.UserBody;
import com.werun.back.entity.UserEntity;
import com.werun.back.entity.UserSchool;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

/**
 * @ClassName userDao
 * @Author HWG
 * @Time 2019/4/19 12:44
 */
public interface userDao {

    //插入一个新用户信息
    @Insert("insert into " +
            "user(u_id,u_phone,u_nickname,u_avatar) " +
            "values(#{uId},#{uPhone},#{uNickname},#{uAvatar});")
    int insert(UserEntity u);

    //根据phone获取用户信息
    @Select("select  u_id,u_phone,u_selfdes,u_nickname,u_gender,u_birthday,u_avatar,u_ex_time,u_rank,u_ex_amount,u_reg_time,u_post_num " +
            "from user where u_phone=#{1};")
    UserEntity getByPhone(@Param("1")String phone);

    //根据uid获取用户信息
    @Select("select  u_id,u_phone,u_selfdes,u_nickname,u_gender,u_birthday,u_avatar,u_ex_time,u_rank,u_ex_amount,u_reg_time,u_post_num " +
            "from user where u_id=#{1};")
    UserEntity getByUid(@Param("1")String uid);

    //根据uid获取用户信息
    @Select("select  u_id,u_phone,u_selfdes,u_nickname,u_gender,u_birthday,u_avatar,u_ex_time,u_rank,u_ex_amount,u_reg_time,u_post_num " +
            "from user order by u_rank desc limit #{1},#{2};")
    List<UserEntity> getAll(@Param("1")int fromIndex, @Param("2")int size);

    //根据uid获取用户信息
    @Select("select count(*) from user;")
    int countAll();

    //插入用户身体信息
    @Insert("insert user_body(u_id) values(#{uId});")
    int insertUserBody(UserBody u);

    //插入用户单位信息
    @Insert("insert user_school(u_id) values(#{uId});")
    int insertUserSchool(UserSchool u);

    //获取用户身体信息
    @Select("select u_id,u_height,u_weight,u_vial_cap from user_body where u_id=#{1};")
    UserBody getUserBodyByUid(@Param("1")String uid);

    //获取用户身体信息数量
    @Select("select COUNT(*) from user_body where u_id=#{1};")
    int getUserBodyNumByUid(@Param("1")String uid);

    //获取用户单位信息
    @Select("select u_id,u_school,u_academy,u_number,u_reg_year,u_edu,u_img from user_school where u_id=#{1};")
    UserSchool getUserSchoolByUid(@Param("1")String uid);

    //更新身体信息
    @Update("update user_body set " +
            "u_height=#{uHeight}," +
            "u_weight=#{uWeight},"+
            "u_vial_cap=#{uVialCap} "+
            " where u_id=#{uId};")
    int updateUBody(UserBody u);

    //更新单位信息
    @Update("update user_school set " +
            "u_school=#{uSchool}," +
            "u_academy=#{uAcademy},"+
            "u_number=#{uNumber},"+
            "u_reg_year=#{uRegYear},"+
            "u_edu=#{uEdu}, "+
            "u_img=#{uImg} "+
            " where u_id=#{uId}")
    int updateUSchool(UserSchool u);
    //更新用户信息
    @Update("update user set " +
            "u_avatar=#{uAvatar}," +
            "u_nickname=#{uNickname},"+
            "u_selfdes=#{uSelfdes},"+
            "u_gender=#{uGender},"+
            "u_birthday=#{uBirthday}"+
            " where u_id=#{uId}")
    int updateUser(UserEntity u);

    //获取用户com like_Num
    @Select("select count(com_like_num) from comment where com_uid=#{1};")
    int comLikeNum(@Param("1")String uid);
    //获取用户com like_Num
    @Select("select count(diary_like_num) from diary where diary_uid=#{1};")
    int diaryLikeNum(@Param("1")String uid);

    //获取回答数
    @Select("select count(*) from comment where com_uid=#{1};")
    int answerNum(@Param("1")String uid);

    //获取动态数
    @Select("select count(*) from diary where diary_uid=#{1};")
    int postNum(@Param("1")String uid);
}

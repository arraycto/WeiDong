package com.werun.back.dao;

import com.werun.back.entity.DiaryEntity;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import javax.jws.WebService;
import java.util.List;

/**
 * @ClassName DiaryDao
 * @Author HWG
 * @Time 2019/4/21 14:16
 */
public interface DiaryDao {

    //插入一个新的diary
    @Insert("insert into diary(diary_id,diary_uid,diary_b_uid,diary_lable,diary_type,diary_title,diary_content,diary_content_preview,diary_img_preview,diary_anonymous) " +
            "values(#{diaryId},#{diaryUid},#{diaryBUid},#{diaryLable},#{diaryType},#{diaryTitle},#{diaryContent},#{diaryContentPreview},#{diaryImgPreview},#{diaryAnonymous});")
    int insert(DiaryEntity d);

    //根据diary_id精确查找
    @Select("select diary_id,diary_uid,diary_b_uid,diary_time,diary_lable,diary_type,diary_title,diary_content,diary_content_preview,diary_img_preview,diary_anonymous,diary_comment_num,diary_read_num,diary_like_num,diary_reward,diary_status " +
            "from diary where diary_id=#{1};")
    DiaryEntity selectByDid(@Param("1") String did);

    //根据uid查找
    @Select("select diary_id,diary_uid,diary_b_uid,diary_time,diary_lable,diary_type," +
            "diary_title,diary_content,diary_content_preview,diary_img_preview,diary_anonymous," +
            "diary_comment_num,diary_read_num,diary_like_num,diary_reward " +
            "from diary where diary_b_uid=#{1} and diary_type=#{2} and diary_status=#{5} order by diary_time desc limit #{3},#{4};")
    List<DiaryEntity> selectByUid(@Param("1") String uid, @Param("2") int type, @Param("3") int fromIndex, @Param("4") int size, @Param("5") int status);
    @Select("select count(*)" +
            "from diary where diary_uid=#{1} and diary_type=#{2} and diary_status=#{3};")
    int countDaren(@Param("1") String uid, @Param("2") int type,@Param("3") int status);

    //根据时间最新查找
    @Select("select diary_id,diary_uid,diary_b_uid,diary_time,diary_lable,diary_type," +
            "diary_title,diary_content,diary_content_preview,diary_img_preview,diary_anonymous," +
            "diary_comment_num,diary_read_num,diary_like_num,diary_reward,diary_status " +
            "from diary where (diary_uid like #{1}) and (diary_type between #{2} and #{3}) and diary_status=1 order by diary_time desc limit #{4},#{5};")
    List<DiaryEntity> selectByTime(@Param("1") String uid, @Param("2") int fromType, @Param("3") int endType, @Param("4") int fromIndex, @Param("5") int size);

    //根据key最新查找
    @Select("select diary_id,diary_uid,diary_b_uid,diary_time,diary_lable,diary_type,diary_title,diary_content,diary_content_preview,diary_img_preview,diary_anonymous,diary_read_num,diary_like_num,diary_reward,diary_status " +
            "from diary where (diary_title like #{1}) and (diary_type between #{2} and #{3}) and diary_status=1 order by diary_time desc limit #{4},#{5};")
    List<DiaryEntity> selectByKey(@Param("1") String key, @Param("2") int fromType, @Param("3") int endType, @Param("4") int fromIndex, @Param("5") int size);

    //根据热度查找
    @Select("select diary_id,diary_uid,diary_b_uid,diary_time,diary_lable,diary_title,diary_content,diary_content_preview,diary_img_preview,diary_anonymous,diary_read_num,diary_like_num,diary_reward,diary_status " +
            "from diary where diary_status=1 and diary_type=#{3} order by diary_comment_num desc,diary_time desc limit #{1},#{2};")
    List<DiaryEntity> selectByHotDegree(@Param("1") int fromIndex, @Param("2") int size, @Param("3") int type);

    //阅读一个diary
    @Update("update diary set diary_read_num=diary_read_num+1 where diary_id=#{1};")
    int read(@Param("1") String did);

    //更改diary状态
    @Update("update diary set diary_status=#{2} where diary_id=#{1};")
    int changeStatus(@Param("1") String did, @Param("2") int status);

    //点赞一个diary
    @Update("update diary set diary_like_num=diary_like_num+1 where diary_id=#{1};")
    int like(@Param("1") String did);

    @Update("update diary set diary_like_num=diary_like_num-1 where diary_id=#{1};")
    int cancleLike(@Param("1") String did);

    //评论一个diary
    @Update("update diary set diary_comment_num=diary_comment_num+1 where diary_id=#{1};")
    int comment(@Param("1") String did);

    //查找符合条件的总量
    @Select("select count(*) from diary where (diary_uid like #{1}) and  (diary_type between #{2} and #{3}) and diary_status=#{4};")
    int count(@Param("1") String uid, @Param("2") int fromType, @Param("3") int endType, @Param("4") int status);

    //查找日记
    @Select("select count(*) from diary where (diary_uid like #{1}) and  (diary_type between #{2} and #{3}) and diary_status=#{4} and diary_anonymous=#{5};")
    int countWithAno(@Param("1") String uid, @Param("2") int fromType, @Param("3") int endType, @Param("4") int status, @Param("5") int ano);

    @Select("select diary_id,diary_uid,diary_b_uid,diary_time,diary_lable,diary_type,diary_title,diary_content,diary_content_preview,diary_img_preview,diary_anonymous,diary_read_num,diary_like_num,diary_reward,diary_status " +
            "from diary where (diary_uid like #{1}) and diary_type = #{2} and diary_status=1 and diary_anonymous=#{3} order by diary_time desc limit #{4},#{5};")
    List<DiaryEntity> selectDiaryByTime(@Param("1") String uid, @Param("2") int type, @Param("3") int ano, @Param("4") int fromIndex, @Param("5") int size);

    //查找符合条件的总量
    @Select("select count(*) from diary where (diary_title like #{1}) and  (diary_type between #{2} and #{3}) and diary_status=#{4};")
    int countKey(@Param("1") String key, @Param("2") int fromType, @Param("3") int endType, @Param("4") int status);

    //圈子-同校
    @Select("select " +
            "diary_id,diary_uid,diary_b_uid,diary_time,diary_lable,diary_type,diary_title,diary_content,diary_content_preview,diary_img_preview,diary_anonymous,diary_comment_num,diary_read_num,diary_like_num,diary_reward,diary_status  " +
            "from diary where diary_uid in " +
            "(select u_id from user_school where u_school =#{1} ) " +
            "and (diary_type=#{2} or diary_type=#{3}) and diary_status=#{4} " +
            "order by diary_time desc limit #{5},#{6};")
    List<DiaryEntity> cicleSameSchool(@Param("1") String school,
                                 @Param("2") int type1,
                                 @Param("3") int type2,
                                 @Param("4") int status,
                                 @Param("5") int fromIndex,
                                 @Param("6") int size);
    @Select("select " +
            "count(*) from diary where diary_uid in " +
            "(select u_id from user_school where u_school =#{1} ) " +
            "and (diary_type=#{2} or diary_type=#{3}) and diary_status=#{4};")
    int countCicleSchool(@Param("1") String school,
                         @Param("2") int type1,
                         @Param("3") int type2,
                         @Param("4") int status);

    //圈子-推荐
    @Select("select " +
            "diary_id,diary_uid,diary_b_uid,diary_time,diary_lable,diary_type,diary_title,diary_content,diary_content_preview,diary_img_preview,diary_anonymous,diary_comment_num,diary_read_num,diary_like_num,diary_reward,diary_status  " +
            "from diary where " +
            " (diary_type=#{1} or diary_type=#{2}) and diary_status=#{3} " +
            "order by diary_time desc limit #{4},#{5};")
    List<DiaryEntity> cicleRecommand(@Param("1") int type1,
                                 @Param("2") int type2,
                                 @Param("3") int status,
                                 @Param("4") int fromIndex,
                                 @Param("5") int size);
    @Select("select " +
            "count(*) from diary where " +
            " (diary_type=#{1} or diary_type=#{2}) and diary_status=#{3};")
    int countCicleRec(@Param("1") int type1,
                      @Param("2") int type2,
                      @Param("3") int status);

    //圈子-热度
    @Select("select " +
            "diary_id,diary_uid,diary_b_uid,diary_time,diary_lable,diary_type,diary_title,diary_content,diary_content_preview,diary_img_preview,diary_anonymous,diary_comment_num,diary_read_num,diary_like_num,diary_reward,diary_status  " +
            "from diary where " +
            " (diary_type=#{1} or diary_type=#{2}) and diary_status=#{3} " +
            "order by diary_like_num desc," +
            "diary_like_num desc," +
            "diary_read_num desc," +
            "diary_time desc" +
            " limit #{4},#{5};")
    List<DiaryEntity> cicleHot(@Param("1") int type1,
                                     @Param("2") int type2,
                                     @Param("3") int status,
                                     @Param("4") int fromIndex,
                                     @Param("5") int size);
    @Select("select " +
            "count(*) from diary where " +
            " (diary_type=#{1} or diary_type=#{2}) and diary_status=#{3};")
    int countCicleHot(@Param("1") int type1,
                      @Param("2") int type2,
                      @Param("3") int status);
}

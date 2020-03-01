package com.werun.back.dao;

import com.werun.back.entity.CommentEntity;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface commentDao {

    @Select("select count(*) from comment where com_item_id=#{1};")
    int count(@Param(value = "1") String did);

    @Select("select " +
            "com_id,com_item_id,com_uid,com_b_uid,com_type,com_content,com_like_num,com_time " +
            "from comment " +
            "where com_item_id=#{1} order by com_time limit #{2},#{3};")
    List<CommentEntity> allComment(@Param(value = "1") String did, @Param(value = "2") int fromIndex, @Param(value = "3") int size);

//    @Select("select * from comment where comment_id=#{1};")
//    CommentEntity selectCommentById(@Param(value = "1") String comment_id);
//
//    @Select("select * from comment where comment_id=(" +
//            "select issue_questions_replyid from issue_questions where issue_questions_id=#{1});")
//    CommentEntity selectReply(@Param(value = "1") String itemid);


    //添加评论
    @Insert("insert into comment" +
            "(com_id,com_item_id,com_uid,com_b_uid,com_type,com_content) " +
            "values" +
            "(#{comId},#{comItemId},#{comUid},#{comBUid},#{comType},#{comContent});")
    int addComment(CommentEntity commentEntity);

//    @Update("update comment set comment_status=#{2} where comment_id=#{1};")
//    int changeCommentStatus(@Param(value = "1") String commentid, @Param(value = "2") int status);
//
//    @Update("update comment set comment_dislike=comment_dislike+1 where comment_id=#{1};")
//    int dislike(@Param(value = "1") String commentid);
//
//    @Update("update comment set comment_dislike=comment_dislike-1 where comment_id=#{1};")
//    int cancelDislike(@Param(value = "1") String commentid);
//
    @Update("update comment set com_like_num=com_like_num + 1 where com_id=#{1};")
    int like(@Param(value = "1") String cid);

    @Update("update comment set com_like_num=com_like_num - 1 where com_id=#{1};")
    int cancelLike(@Param(value = "1") String cid);
}

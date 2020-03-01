package com.werun.back.service;

import com.werun.back.dao.likeDao;
import com.werun.back.entity.LikeEntity;
import com.werun.back.entity.PageInfo;
import com.werun.back.entity.UserEntity;
import com.werun.back.enums.ExceptionsEnum;
import com.werun.back.exception.WeRunException;
import javassist.expr.NewArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName LikeServ
 * @Author HWG
 * @Time 2019/4/23 14:56
 */
@Service
public class LikeServ {
    @Resource
    private likeDao likeDao;
    @Autowired
    private UserServ userServ;
    //插入一个like
    public int insert(LikeEntity like)throws Exception{
        int rel;
        try {
            rel=likeDao.insert(like);
        }catch (Exception e){
            e.printStackTrace();
            throw new WeRunException(ExceptionsEnum.LIKEINSERTFAILED);
        }
        return rel;
    }

    //查询
    public LikeEntity selectByLidAndUid(String lid,String uid)throws Exception{
        LikeEntity like;
        try {
            like=likeDao.queryLikeByUid(lid,uid);
        }catch (Exception e){
            e.printStackTrace();
            throw new WeRunException(ExceptionsEnum.LIKENODATA);
        }
        return like;
    }

    //批量获取
    public List<LikeEntity> selectByLid(String lid, int type, int status, PageInfo pageInfo)throws Exception{
        List<LikeEntity> likes;
        try {
            likes=likeDao.queryByLid(lid,type,status, pageInfo.getFromIndex(),pageInfo.getPageSize());
        }catch (Exception e){
            e.printStackTrace();
            throw new WeRunException(ExceptionsEnum.LIKENODATA);
        }
        return likes;
    }

    //批量获取dian赞用户列表
    public List<UserEntity> selectUserList(String lid, int type, int status, PageInfo pageInfo)throws Exception{
        List<LikeEntity> likeEntities = selectByLid(lid, type, status, pageInfo);
        List<UserEntity> users= new ArrayList<>();
        for(LikeEntity like:likeEntities){
            users.add(userServ.getByUid(like.getLikeUid()));
        }
        return users;
    }
    //count
    public int count(String lid,int type,int status)throws Exception{
        int co;
        try {
            co=likeDao.count(lid,type,status);
        }catch (Exception e){
            e.printStackTrace();
            throw new WeRunException(ExceptionsEnum.LIKENODATA);
        }
        return co;
    }

    //添加或取消
    public int reLike(String uid,String lid,int type)throws Exception{
        LikeEntity like = selectByLidAndUid(lid, uid);
        int re=0;
        if (like==null){
            re=insert(new LikeEntity(lid,uid,type));
        }else{
            if(like.getLikeType()==type){
                re=(like.getLikeStatus()==1) ? 2:1;
                likeDao.relike(uid,lid,re);
            }else
                throw new WeRunException(ExceptionsEnum.LIKELIKED);
        }
        return re;
    }

}

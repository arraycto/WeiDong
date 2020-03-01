package com.werun.back.service;

import com.werun.back.VO.ComUserVO;
import com.werun.back.dao.commentDao;
import com.werun.back.entity.CommentEntity;
import com.werun.back.entity.LikeEntity;
import com.werun.back.entity.PageInfo;
import com.werun.back.enums.ExceptionsEnum;
import com.werun.back.exception.WeRunException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName ComServ
 * @Author HWG
 * @Time 2019/4/23 17:04
 */
@Service
public class ComServ {
    @Resource
    private commentDao commentDao;
    @Autowired
    private UserServ userServ;
    @Autowired
    private LikeServ likeServ;
    @Autowired
    private AnnexServ annexServ;

    //插入
    public int insert(CommentEntity comment)throws Exception{
        int re;
        try {
            re=commentDao.addComment(comment);
        }catch (Exception e){
            e.printStackTrace();
            throw new WeRunException(ExceptionsEnum.COMINSERTFAILED);
        }
        return re;
    }

    //查总数
    public int count(String did)throws Exception{
        int cou;
        try {
            cou=commentDao.count(did);
        }catch (Exception e){
            e.printStackTrace();
            throw new WeRunException(ExceptionsEnum.COMNODATA);
        }
        return cou;
    }

    //列表
    public List<CommentEntity> selectByDid(String did, PageInfo pageInfo)throws Exception{
        List<CommentEntity> coms;
        try {
            coms=commentDao.allComment(did,pageInfo.getFromIndex(),pageInfo.getPageSize());
        }catch (Exception e){
            e.printStackTrace();
            throw new WeRunException(ExceptionsEnum.COMNODATA);
        }
        return coms;
    }

    //评论用户列表
    public List<ComUserVO> selectUserByDid(String did,String uid,PageInfo pageInfo)throws Exception{
        List<ComUserVO> cuvs=new ArrayList<>();
        List<CommentEntity> comments = selectByDid(did, pageInfo);
        for(CommentEntity com:comments){
            ComUserVO cuv=new ComUserVO(com);
            cuv.setUser(userServ.getByUid(cuv.getComUid()));
            LikeEntity likeEntity = likeServ.selectByLidAndUid(com.getComId(), uid);
            if(likeEntity!=null&&likeEntity.getLikeStatus()==1)
                cuv.setRepository(1);
            List<String> strings = annexServ.selectAnnexPath(com.getComId(), 2);
            cuv.setImgs(strings);
            cuvs.add(cuv);
        }
        return cuvs;
    }
    //点赞和取消
    public int like(String cid)throws Exception{
        try {
            return commentDao.like(cid);
        }catch (Exception e){
            e.printStackTrace();
            throw new WeRunException(ExceptionsEnum.LIKEINSERTFAILED);
        }
    }
    public int cancleLike(String cid)throws Exception{
        try {
            return commentDao.cancelLike(cid);
        }catch (Exception e){
            e.printStackTrace();
            throw new WeRunException(ExceptionsEnum.LIKEINSERTFAILED);
        }
    }
}

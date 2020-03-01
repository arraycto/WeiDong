package com.werun.back.service;

import com.werun.back.VO.ActUserVO;
import com.werun.back.dao.ActivityDao;
import com.werun.back.entity.ActivityEntity;
import com.werun.back.entity.PageInfo;
import com.werun.back.entity.UserEntity;
import com.werun.back.enums.ExceptionsEnum;
import com.werun.back.exception.WeRunException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @ClassName ActServ
 * @Author HWG
 * @Time 2019/4/22 16:53
 */
@Service
public class ActServ {
    @Resource
    private ActivityDao actdao;
    @Value("${FileOp.werunImg.head}")
    private String headPath;
    //新建活动
    public int insert(ActivityEntity act)throws Exception{
        int result;
        try {
            result=actdao.insert(act);
        }catch (Exception e){
            e.printStackTrace();
            throw new WeRunException(ExceptionsEnum.ACTINSERTFAILED);
        }
        join(act.getActUid(),act.getActId());
        return result;
    }
    //改变活动状态
    public int changeActStatus(String uid,String aid,int status)throws Exception{
        int result;
            ActivityEntity activityEntity = selectByAid(aid);
            if(!activityEntity.getActUid().equals(uid))
                throw new WeRunException(ExceptionsEnum.NOAUTHORITY);
            try{
                result=actdao.changeActStatus(aid,2);
            }catch (Exception e){
                e.printStackTrace();
                throw new  WeRunException(ExceptionsEnum.ACTCHANGEFAILED);
            }
        return result;

    }
    //根据aid 查询一个act +头像处理
    public ActivityEntity selectByAid(String aid)throws Exception{
        ActivityEntity act;
        try {
            act=actdao.selectByAid(aid);
            if (act==null)
                throw new WeRunException(ExceptionsEnum.ACTWRONGAID);
            act.setActPreviewImg(headPath+act.getActPreviewImg());
        }catch (Exception e){
            if(e instanceof WeRunException)
                throw new WeRunException(ExceptionsEnum.ACTWRONGAID);
            else
            {
                e.printStackTrace();
                throw new WeRunException(ExceptionsEnum.ACTWRONGAID);
            }
        }

        return act;
    }
    //查询关系
    public ActUserVO selectByAidAndUid(String aid, UserEntity user)throws Exception{
        ActivityEntity activityEntity = selectByAid(aid);
        ActUserVO auv=new ActUserVO(activityEntity);
        auv.setUser(user);
        Object i2 = actdao.queryRepository(aid, user.getuId());
        int i;
        if(i2==null){
            auv.setRepository(1);
        }else {
            i=(int)i2;
            if(activityEntity.getActUid().equals(user.getuId()))
                auv.setRepository(4);
            else if(i==1){
                auv.setRepository(2);
            }else if(i==2){
                auv.setRepository(3);
            }
            System.out.println("------"+i+"----------");
        }

        return auv;
    }
    //参加活动
    public int join(String uid,String aid)throws Exception{
        int result;
        try {
            Object o = actdao.queryRepository(aid, uid);
            if(o==null){
                result=actdao.insertIntoUser2Team(aid,uid);
                actdao.join(aid);
            }else if(((int)o==1))
                return 1;
            else if (((int)o==2)){
                result=actdao.reJoin(aid,uid);
                actdao.join(aid);
            }else
                return 0;
        }catch (Exception e){
            e.printStackTrace();
            throw new WeRunException(ExceptionsEnum.ACTJOINFAILED);
        }
        return result;
    }
    //退出活动
    public int out(String uid,String aid)throws Exception{
        int result;
        try {
            result=actdao.logoutTeam(aid,uid);
            actdao.logout(aid);
        }catch (Exception e){
            e.printStackTrace();
            throw new WeRunException(ExceptionsEnum.ACTOUTFAILED);
        }
        return result;
    }
    //查总量
    public int count()throws Exception{
        int result;
        try {
            result=actdao.count();
        }catch (Exception e){
            e.printStackTrace();
            throw new WeRunException(ExceptionsEnum.ACTINSERTFAILED);
        }
        return result;
    }
    //查列表 +头像处理
    public List<ActivityEntity> list(PageInfo pageInfo)throws Exception{
        List<ActivityEntity> acts;
        try {
            acts=actdao.selectByTime(pageInfo.getFromIndex(),pageInfo.getPageSize());
            if(acts!=null){
                for(ActivityEntity act:acts){
                    act.setActPreviewImg(headPath+act.getActPreviewImg());
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            throw new WeRunException(ExceptionsEnum.ACTWRONGAID);
        }
        return acts;
    }
}

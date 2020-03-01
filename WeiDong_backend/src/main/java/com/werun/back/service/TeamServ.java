package com.werun.back.service;

import com.werun.back.VO.TeamUserVO;
import com.werun.back.dao.TeamDao;
import com.werun.back.entity.PageInfo;
import com.werun.back.entity.TeamEntity;
import com.werun.back.entity.UserEntity;
import com.werun.back.enums.ExceptionsEnum;
import com.werun.back.exception.WeRunException;
import org.springframework.beans.factory.annotation.Autowired;
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
public class TeamServ {
//    @Resource
//    private ActivityDao actdao;
    @Resource
    private TeamDao teamDao;
    @Autowired
    private UserServ userServ;
    @Value("${FileOp.werunImg.head}")
    private String headPath;
    //新建小组
    public int insert(TeamEntity team)throws Exception{
        int result;
        try {
            result=teamDao.insert(team);
        }catch (Exception e){
            e.printStackTrace();
            throw new WeRunException(ExceptionsEnum.ACTINSERTFAILED);
        }
        join(team.getTeamUid(),team.getTeamId());
        return result;
    }
    //改变小组状态
    public int changeTeamStatus(String uid,String tid,int status)throws Exception{
        int result;
            TeamEntity team = selectByTid(tid);
            if(!team.getTeamUid().equals(uid))
                throw new WeRunException(ExceptionsEnum.NOAUTHORITY);
            try{
                result=teamDao.changeStatus(team.getTeamId(),status);
            }catch (Exception e){
                e.printStackTrace();
                throw new  WeRunException(ExceptionsEnum.ACTCHANGEFAILED);
            }
        return result;

    }
    //根据aid 查询一个act +头像处理
    public TeamEntity selectByTid(String tid)throws Exception{
        TeamEntity team;
        try {
            team=teamDao.selectByTid(tid,1);
            if (team==null)
                throw new WeRunException(ExceptionsEnum.ACTWRONGAID);
            team.setActAvatar(headPath+team.getActAvatar());
        }catch (Exception e){
            if(e instanceof WeRunException)
                throw new WeRunException(ExceptionsEnum.ACTWRONGAID);
            else
            {
                e.printStackTrace();
                throw new WeRunException(ExceptionsEnum.ACTWRONGAID);
            }
        }

        return team;
    }
    //查询关系
    public TeamUserVO selectByTidAndUid(String tid, UserEntity user)throws Exception{
        TeamEntity team = selectByTid(tid);
        UserEntity reuser = userServ.getByUid(team.getTeamUid());
        TeamUserVO tuv=new TeamUserVO(team);
        tuv.setUser(reuser);
        Object o = teamDao.queryRepository(tid, user.getuId());
        int i;
        if(o==null){
            tuv.setRepository(1);
        }else {
            i=(int)o;
            if(team.getTeamUid().equals(user.getuId()))
                tuv.setRepository(4);
            else if(i==1){
                tuv.setRepository(2);
            }else if(i==2){
                tuv.setRepository(3);
            }
        }

        return tuv;
    }
    //加入小组
    public int join(String uid,String tid)throws Exception{
        int result;
        try {
            Object o = teamDao.queryRepository(tid,uid);
            if(o==null){
                result=teamDao.uJoin(tid,uid);
                teamDao.join(tid);
            }else if(((int)o==1))
                return 1;
            else if (((int)o==2)){
                result= teamDao.reJoin(tid,uid);
                teamDao.join(tid);
            }else
                return 0;
        }catch (Exception e){
            e.printStackTrace();
            throw new WeRunException(ExceptionsEnum.ACTJOINFAILED);
        }
        return result;
    }
    //退出小组
    public int out(String uid,String tid)throws Exception{
        int result;
        try {
            teamDao.logoutTeam(tid,uid);
            result=teamDao.out(tid);
        }catch (Exception e){
            e.printStackTrace();
            throw new WeRunException(ExceptionsEnum.ACTOUTFAILED);
        }
        return result;
    }
    //查总量--推荐
    public int count()throws Exception{
        int result;
        try {
            result=teamDao.count();
        }catch (Exception e){
            e.printStackTrace();
            throw new WeRunException(ExceptionsEnum.ACTINSERTFAILED);
        }
        return result;
    }
    //查总量--推荐
    public int count(String keyword)throws Exception{
        int result;
        try {
            result=teamDao.countSearch("%"+keyword+"%");
        }catch (Exception e){
            e.printStackTrace();
            throw new WeRunException(ExceptionsEnum.ACTINSERTFAILED);
        }
        return result;
    }
    //查列表 +头像处理
    public List<TeamEntity> list(PageInfo pageInfo)throws Exception{
        List<TeamEntity> teams;
        try {
            teams = teamDao.selectByTime(1, pageInfo.getFromIndex(), pageInfo.getPageSize());
            if(teams!=null){
                for(TeamEntity team:teams){
                    team.setActAvatar(headPath+team.getActAvatar());
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            throw new WeRunException(ExceptionsEnum.ACTWRONGAID);
        }
        return teams;
    }
    //搜索结果
    public List<TeamEntity> search(String keyWord,PageInfo pageInfo)throws Exception{
        List<TeamEntity> teams;
        try {
            teams = teamDao.selectByName(1, "%"+keyWord+"%",pageInfo.getFromIndex(), pageInfo.getPageSize());
            if(teams!=null){
                for(TeamEntity team:teams){
                    team.setActAvatar(headPath+team.getActAvatar());
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            throw new WeRunException(ExceptionsEnum.ACTWRONGAID);
        }
        return teams;
    }
}

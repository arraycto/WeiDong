package com.werun.back.service;

import com.werun.back.dao.ExerciseDao;
import com.werun.back.entity.ExerciseEntity;
import com.werun.back.entity.PageInfo;
import com.werun.back.enums.ExceptionsEnum;
import com.werun.back.exception.WeRunException;
import com.werun.back.utils.TimeUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @ClassName ExServ
 * @Author HWG
 * @Time 2019/4/26 15:13
 */

@Service
public class ExServ {
    @Resource
    private ExerciseDao exerciseDao;
    //插入数据
    public int insert(ExerciseEntity ex)throws Exception{
        int result;
        try {
            result=exerciseDao.insert(ex);
        }catch (Exception e){
            e.printStackTrace();
            throw new WeRunException(ExceptionsEnum.EXINSERTFAILD);
        }
        return result;
    }

    //查询数据
    public List<ExerciseEntity> select(String uid,String fDate)throws Exception{
        Date ed = TimeUtil.getDateByYMD(fDate);
        Date fd;
        List<ExerciseEntity> exs=new ArrayList<>();
        ExerciseEntity exerciseEntity1 = selectLastest(uid, TimeUtil.getFormatyMdHms(ed));
        if(exerciseEntity1==null)
            return exs;
        else {
            ed=TimeUtil.getDateByYMD(exerciseEntity1.getExStartTime().substring(0,10));
            ed=new Date(ed.getTime()+ (long) (60*60*24*1000));
        }
        for(int i=0;i<30;i++){
            fd=new Date(ed.getTime() - (long) (60*60*24*1000));
            ExerciseEntity exerciseEntity = selectOne(uid, TimeUtil.getFormatyMdHms(fd), TimeUtil.getFormatyMdHms(ed));
            if(exerciseEntity!=null){
                exerciseEntity.setExStartTime(TimeUtil.getFormatyMdHms(fd));
                exs.add(exerciseEntity);
            }
            ed=new Date(ed.getTime() - (long) (60*60*24*1000));
        }
        return exs;
    }

    //查询某一天的数据
    public ExerciseEntity selectOne(String uid,String fd,String ed)throws Exception{
        try {
            ExerciseEntity select = exerciseDao.select(uid, fd, ed);
            return select;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    //检查是否还有数据
    public boolean checkNum(String uid,String t){
        try {
            int i = exerciseDao.checkNum(uid, t);
//            System.out.println("-------------------------"+i);
            if(i>0)
                return true;
        }catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }
    //查询最近的一条数据
    public ExerciseEntity selectLastest(String uid,String fd)throws Exception{
        try {
            ExerciseEntity select = exerciseDao.selectLastest(uid, fd);
            return select;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public int updateExData(String uid,String st,String et)throws Exception{
        return 0;
    }

    public int count(String uid,String etime)throws Exception{
        try {
            return exerciseDao.count(etime,uid);
        }catch (Exception e){
            e.printStackTrace();
            throw new WeRunException(ExceptionsEnum.NODATA);
        }

    }

    public List<ExerciseEntity> select(String uid, String etime, PageInfo page)throws Exception{
        List<ExerciseEntity> data=new ArrayList<>();
        try {
            data = exerciseDao.selectDtl(etime,page.getFromIndex(),page.getPageSize(),uid);
        }catch (Exception e){
            e.printStackTrace();
            throw new WeRunException(ExceptionsEnum.NODATA);
        }
        return data;
    }
}







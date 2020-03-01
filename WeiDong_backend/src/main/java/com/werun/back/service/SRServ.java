package com.werun.back.service;

import com.werun.back.dao.SRDao;
import com.werun.back.entity.PageInfo;
import com.werun.back.entity.SchoolRank;
import com.werun.back.enums.ExceptionsEnum;
import com.werun.back.exception.WeRunException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @ClassName SRServ
 * @Author HWG
 * @Time 2019/5/2 9:44
 */
@Service
public class SRServ {
    @Resource
    private SRDao srDao;

    //查排名
    public List<SchoolRank> selectToday(String today, PageInfo page)throws Exception{
        try {
            return srDao.selectToday(today, page.getFromIndex(), page.getPageSize());
        }catch (Exception e){
            e.printStackTrace();
            throw new WeRunException(ExceptionsEnum.NODATA);
        }
    }
    //查数量
    public int countToday(String today)throws Exception{
        try {
            return srDao.countToday(today);
        }catch (Exception e){
            e.printStackTrace();
            throw new WeRunException(ExceptionsEnum.NODATA);
        }

    }
}

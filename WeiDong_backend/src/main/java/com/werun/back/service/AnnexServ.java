package com.werun.back.service;

import com.werun.back.dao.AnnexDao;
import com.werun.back.entity.AnnexEntity;
import com.werun.back.enums.ExceptionsEnum;
import com.werun.back.exception.WeRunException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName AnnexServ
 * @Author HWG
 * @Time 2019/4/20 10:11
 */
@Service
public class AnnexServ {
    @Resource
    private AnnexDao annexDao;
    @Value("${FileOp.werunImg.good}")
    private String goodsPath;
    @Value("${FileOp.werunImg.post}")
    private String postPath;

    public int insert(String annex,String pid)throws Exception{
        int i=0;
        String[] annexs = annex.split(",");
        try{
        for(String a:annexs){
            if(a!=null&&a.length()>0){
                annexDao.insert(new AnnexEntity(pid,a,++i));
            }
        }
        }catch (Exception e){
            e.printStackTrace();
            throw new WeRunException(ExceptionsEnum.ANNEXINSERTFAILED);
        }
        return i;
    }

    public List<AnnexEntity> selectAnnexs(String pid)throws Exception{
        List<AnnexEntity> annexs;
        try {
            annexs = annexDao.selectByPid(pid);
        }catch (Exception e){
            e.printStackTrace();
            throw new WeRunException(ExceptionsEnum.NODATA);
        }
        return annexs;
    }

    //查询附件地址  type：1商品附件  2动态附件
    public List<String> selectAnnexPath(String pid,int type)throws Exception{
        List<String> path=new ArrayList<>();
        List<AnnexEntity> annexs;
        try {
            annexs = annexDao.selectByPid(pid);
            switch (type){
                case 1:
                    for (AnnexEntity a:annexs) {
                        path.add(goodsPath+a.getAnnexName());
                    }
                    break;
                case 2:
                    for (AnnexEntity a:annexs) {
                        path.add(postPath+a.getAnnexName());
                    }
                    break;
                default:
                    throw new WeRunException(ExceptionsEnum.NOANNEXPATH);
            }
        }catch (Exception e){
            e.printStackTrace();
            throw new WeRunException(ExceptionsEnum.NODATA);
        }
        return path;
    }

}

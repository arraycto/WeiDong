package com.werun.back.service;

import com.werun.back.dao.AddressDao;
import com.werun.back.entity.AddressEntity;
import com.werun.back.enums.ExceptionsEnum;
import com.werun.back.exception.WeRunException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @ClassName AddServ
 * @Author HWG
 * @Time 2019/4/26 23:07
 */
@Service
public class AddServ {
    @Resource
    private AddressDao addressDao;
    //insert
    public int insert(AddressEntity add)throws Exception{
        int count = addressDao.getCount(add.getAdUid(), 1);
        if(count>=5)
            throw  new WeRunException(ExceptionsEnum.ADDNUMTOOMUCH);
        try {
            addressDao.insert(add);
        }catch (Exception e){
            e.printStackTrace();
            throw  new WeRunException(ExceptionsEnum.ADDINSERTFAILED);
        }
        return 1;
    }
    //get default address
    public AddressEntity getDefault(String uid)throws Exception{
        try {
            AddressEntity defaultAdd = addressDao.getDefaultAdd(uid, 1);
            return defaultAdd;
        }catch (Exception e){
            e.printStackTrace();
            throw new WeRunException(ExceptionsEnum.ADDNOADD);
        }
    }
    //get by aid
    public AddressEntity getByAid(String aid)throws Exception{
        try {
            AddressEntity defaultAdd = addressDao.getByAid(aid);
            return defaultAdd;
        }catch (Exception e){
            e.printStackTrace();
            throw new WeRunException(ExceptionsEnum.ADDNOADD);
        }
    }
    //get add list
    public List<AddressEntity> getAddList(String uid)throws Exception{
        try {
            List<AddressEntity> addList = addressDao.getAddList(uid, 1);
            return addList;
        }catch (Exception e){
            e.printStackTrace();
            throw new WeRunException(ExceptionsEnum.ADDNOADD);
        }
    }
    //set default
    public int setDefault(String aid,String uid)throws Exception{
        AddressEntity byAid = getByAid(aid);
        if(byAid==null)
            throw new WeRunException(ExceptionsEnum.ADDNOADD);
        if(!byAid.getAdUid().equals(uid))
            throw new WeRunException(ExceptionsEnum.ADDNOAUTHORY);
        try {
            addressDao.setUndefault(uid,1);
            addressDao.setDefault(aid);
        }catch (Exception e){
            e.printStackTrace();
            throw new WeRunException(ExceptionsEnum.ADDSETDEFAILED);
        }
        return 1;
    }
    //delete
    public int delete(String aid,String uid)throws Exception{
        AddressEntity byAid = getByAid(aid);
        if(byAid==null)
            throw new WeRunException(ExceptionsEnum.ADDNOADD);
        if(!byAid.getAdUid().equals(uid))
            throw new WeRunException(ExceptionsEnum.ADDNOAUTHORY);
        try {
            addressDao.delete(aid);
        }catch (Exception e){
            e.printStackTrace();
            throw new WeRunException(ExceptionsEnum.ADDINSERTFAILED);
        }
        return 1;
    }

}

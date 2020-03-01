package com.werun.back.service;

import com.werun.back.VO.GoodUserAnnexVO;
import com.werun.back.VO.GoodsUserEntity;
import com.werun.back.dao.goodDao;
import com.werun.back.entity.GoodsEntity;
import com.werun.back.entity.PageInfo;
import com.werun.back.entity.UserEntity;
import com.werun.back.enums.ExceptionsEnum;
import com.werun.back.exception.WeRunException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName GoodsServ
 * @Author HWG
 * @Time 2019/4/20 8:22
 */
@Service
public class GoodsServ {
    @Resource
    private goodDao gooddao;
    @Autowired
    private UserServ userServ;
    @Autowired
    private AnnexServ annexServ;
    @Value("${FileOp.werunImg.good}")
    private String goodsImgPath;

    public int insert(GoodsEntity g)throws Exception{
        int insert;
        try {
            insert = gooddao.insert(g);
        }catch (Exception e){
            e.printStackTrace();
            throw new WeRunException(ExceptionsEnum.GOODINSERTFAILED);
        }
        return insert;
    }
    //返回商品详细信息{user+annex+good}
    public GoodUserAnnexVO selectGoodInfoByGid(String gid)throws Exception{

        GoodUserAnnexVO guavo;
        GoodsEntity good;
        GoodsUserEntity goodUser;
        List<String> annex;
        try {
            good = gooddao.selectByGid(gid);
            good.setgPreviewImg(goodsImgPath+good.getgPreviewImg());
            goodUser =new GoodsUserEntity(good);
        }catch (Exception e){
            e.printStackTrace();
            throw new WeRunException(ExceptionsEnum.NOGOODDATA);
        }
        try {
            goodUser.setUser(userServ.getByUid(good.getgUid()));
        }catch (Exception e){
            e.printStackTrace();
            throw new WeRunException(ExceptionsEnum.NOUSERDATA);
        }
        try {
            annex=annexServ.selectAnnexPath(good.getgId(),1);
        }catch (Exception e){
            e.printStackTrace();
            throw new WeRunException(ExceptionsEnum.NOUSERDATA);
        }
        guavo=new GoodUserAnnexVO(goodUser);
        guavo.setAnnex(annex);
        return guavo;
    }
    //获取指定商品
    public GoodsEntity selectGoodByGid(String gid)throws Exception{
        GoodsEntity good;
        try {
            good=gooddao.selectByGid(gid);
            if(good!=null){
                good.setgPreviewImg(goodsImgPath+good.getgPreviewImg());
            }
        }catch (Exception e){
            e.printStackTrace();
            throw new WeRunException(ExceptionsEnum.NOGOODDATA);
        }
        return good;
    }
    //获取商品页
    public List<GoodsUserEntity> selectGoodByType(int type, int status, PageInfo pageInfo)throws Exception{
        List<GoodsEntity> goods;
        List<GoodsUserEntity> data=new ArrayList<>();
        try {
            goods = gooddao.selectByType(type, status, pageInfo.getFromIndex(), pageInfo.getPageSize());
            for (GoodsEntity good:goods) {
                good.setgPreviewImg(goodsImgPath+good.getgPreviewImg());
            }
        }catch (Exception e){
            e.printStackTrace();
            throw new WeRunException(ExceptionsEnum.NOGOODDATA);
        }

        try {
            for (GoodsEntity good:goods) {
//                System.out.println(good.toString());
                UserEntity user = userServ.getByUid(good.getgUid());
                GoodsUserEntity gu=new GoodsUserEntity(good);
                gu.setUser(user);
                data.add(gu);
            }
        }catch (Exception e){
            e.printStackTrace();
            throw new WeRunException(ExceptionsEnum.NOUSERDATA);
        }


        return data;
    }
    //取得符合条件商品总数量
    public int getGoodNum(int type,int status)throws Exception{
        int count;
        try {
            count=gooddao.selectGoodNum(type,status);
        }catch (Exception e){
            e.printStackTrace();
            throw new WeRunException(ExceptionsEnum.NODATA);
        }
        return count;
    }
    //购买
    public int buy(String gid,int num)throws Exception{
        int result;
        try {
            result=gooddao.buyGood(gid,num);
        }catch (Exception e){
            e.printStackTrace();
            throw new WeRunException(ExceptionsEnum.UPDATESTOCKFAILED);
        }
        return  result;
    }
    //更新状态
    public int changeStatus(String gid,int status,String uid)throws Exception{
        int result;
        GoodsEntity goodsEntity = gooddao.selectByGid(gid);
        if(goodsEntity==null)
            throw new WeRunException(ExceptionsEnum.NOGOODDATA);
        else if(!uid.equals(goodsEntity.getgUid()))
            throw new WeRunException(ExceptionsEnum.NOAUTHORITY);
        try {
            result=gooddao.changeStatus(gid,status);
        }catch (Exception e){
            e.printStackTrace();
            throw new WeRunException(ExceptionsEnum.UPDATEGOODSTATUSFAILED);
        }
        return result;
    }

}

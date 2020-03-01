package com.werun.back.service;

import com.werun.back.VO.DiaryUserVO;
import com.werun.back.dao.DiaryDao;
import com.werun.back.dao.userDao;
import com.werun.back.entity.*;
import com.werun.back.enums.ExceptionsEnum;
import com.werun.back.exception.WeRunException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName DiaryServ
 * @Author HWG
 * @Time 2019/4/21 14:38
 */
@Service
public class DiaryServ {
    @Resource
    private DiaryDao diaryDao;
    @Value("${FileOp.werunImg.post}")
    private String postPath;
    @Resource
    private UserServ userServ;
    @Autowired
    private AnnexServ annexServ;
    @Autowired
    private LikeServ likeServ;
    @Resource
    private userDao userdao;

    //插入
    public int insert(DiaryEntity d) throws Exception {
        int result;
        try {
            result = diaryDao.insert(d);
        } catch (Exception e) {
            e.printStackTrace();
            throw new WeRunException(ExceptionsEnum.DIARYINSERTFAILED);
        }
        return result;
    }

    //根据id精确查找
    public DiaryEntity selectByDid(String did) throws Exception {
        DiaryEntity diary;
        try {
            diary = diaryDao.selectByDid(did);
        } catch (Exception e) {
            e.printStackTrace();
            throw new WeRunException(ExceptionsEnum.DIARYNODATA);
        }
        return diary;
    }

    //根据id
    //根据时间显示，默认按照时间最新排序
    public List<DiaryUserVO> selectByTime(String uid, int fromType, int entType, PageInfo pageInfo) throws Exception {
        List<DiaryEntity> diarys;
        List<DiaryUserVO> data = new ArrayList<>();
        try {
            diarys = selectByTime(uid, fromType, pageInfo);
            for (DiaryEntity d : diarys) {
                data.add(new DiaryUserVO(d));
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new WeRunException(ExceptionsEnum.DIARYNODATA);
        }
        //查user
        try {
            for (DiaryUserVO duv : data) {
                duv.setUser(userServ.getByUid(duv.getDiaryUid()));
                duv.setBuser(userServ.getByUid(duv.getDiaryBUid()));
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new WeRunException(ExceptionsEnum.DIARYNODATA);
        }

        //annex
        try {
            for (DiaryUserVO duv : data) {
                duv.setImg(annexServ.selectAnnexPath(duv.getDiaryId(), 2));
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new WeRunException(ExceptionsEnum.DIARYNODATA);
        }

        return data;

    }

    //搜索
    public List<DiaryUserVO> search(String key, int fromType, PageInfo pageInfo) throws Exception {
        List<DiaryEntity> diarys;
        List<DiaryUserVO> data = new ArrayList<>();
        try {
            diarys = diaryDao.selectByKey("%" + key + "%", fromType, fromType, pageInfo.getFromIndex(), pageInfo.getPageSize());
            for (DiaryEntity d : diarys) {
                d.setDiaryImgPreview(postPath+d.getDiaryImgPreview());
                data.add(new DiaryUserVO(d));
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new WeRunException(ExceptionsEnum.DIARYNODATA);
        }
        //查user
        try {
            for (DiaryUserVO duv : data) {
                duv.setUser(userServ.getByUid(duv.getDiaryUid()));
                duv.setBuser(userServ.getByUid(duv.getDiaryBUid()));
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new WeRunException(ExceptionsEnum.DIARYNODATA);
        }

        //annex
        try {
            for (DiaryUserVO duv : data) {
                duv.setImg(annexServ.selectAnnexPath(duv.getDiaryId(), 2));
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new WeRunException(ExceptionsEnum.DIARYNODATA);
        }

        return data;

    }

    //根据时间
    public List<DiaryEntity> selectByTime(String uid, int type, PageInfo pageInfo) throws Exception {
        List<DiaryEntity> diarys;
        try {
            diarys = diaryDao.selectByTime(uid, type, type, pageInfo.getFromIndex(), pageInfo.getPageSize());
            for (DiaryEntity d : diarys) {
                d.setDiaryImgPreview(postPath + d.getDiaryImgPreview());
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new WeRunException(ExceptionsEnum.DIARYNODATA);
        }
        return diarys;
    }

    //根据热度查询，以评论人数为主，阅读人数为辅
    public List<DiaryUserVO> selectByHotDegree(PageInfo pageInfo, int type) throws Exception {
        List<DiaryEntity> diarys;
        List<DiaryUserVO> data = new ArrayList<>();
        //查diary
        try {
            diarys = selectByHotDeg(pageInfo, type);
            for (DiaryEntity d : diarys) {
                data.add(new DiaryUserVO(d));
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new WeRunException(ExceptionsEnum.DIARYNODATA);
        }
        //查user
        try {
            for (DiaryUserVO duv : data) {
                duv.setUser(userServ.getByUid(duv.getDiaryUid()));
                duv.setBuser(userServ.getByUid(duv.getDiaryBUid()));
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new WeRunException(ExceptionsEnum.DIARYNODATA);
        }

        //annex
        try {
            for (DiaryUserVO duv : data) {
                duv.setImg(annexServ.selectAnnexPath(duv.getDiaryId(), 2));
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new WeRunException(ExceptionsEnum.DIARYNODATA);
        }


        return data;

    }

    //根据热度
    public List<DiaryEntity> selectByHotDeg(PageInfo pageInfo, int type) throws Exception {
        List<DiaryEntity> diarys;
        //查diary
        try {
            diarys = diaryDao.selectByHotDegree(pageInfo.getFromIndex(), pageInfo.getPageSize(), type);
            for (DiaryEntity d : diarys) {
                d.setDiaryImgPreview(postPath + d.getDiaryImgPreview());
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new WeRunException(ExceptionsEnum.DIARYNODATA);
        }
        return diarys;
    }

    //阅读一个diary
    public int read(String did) throws Exception {
        int result;
        try {
            result = diaryDao.read(did);
        } catch (Exception e) {
            e.printStackTrace();
            throw new WeRunException(ExceptionsEnum.DIARYREADFAILED);
        }
        return result;
    }

    //点赞一个diary
    public int like(String did) throws Exception {
        int result;
        try {
            result = diaryDao.like(did);
        } catch (Exception e) {
            e.printStackTrace();
            throw new WeRunException(ExceptionsEnum.DIARYLIKEFAILED);
        }
        return result;
    }

    //取消点赞
    public int cancleLike(String did) throws Exception {
        int result;
        try {
            result = diaryDao.cancleLike(did);
        } catch (Exception e) {
            e.printStackTrace();
            throw new WeRunException(ExceptionsEnum.DIARYLIKEFAILED);
        }
        return result;
    }

    //评论一个diary
    public int comment(String did) throws Exception {
        int result;
        try {
            result = diaryDao.comment(did);
        } catch (Exception e) {
            e.printStackTrace();
            throw new WeRunException(ExceptionsEnum.DIARYCOMMENTFAILED);
        }
        return result;
    }

    //查询符合条件的总数量
    public int count(String uid, int fromType, int endType, int status) throws Exception {
        int result;
        try {
            result = diaryDao.count(uid, fromType, endType, status);
        } catch (Exception e) {
            e.printStackTrace();
            throw new WeRunException(ExceptionsEnum.DIARYNODATA);
        }
        return result;
    }

    public int countKey(String key, int fromType, int endType) throws Exception {
        int result;
        try {
            result = diaryDao.countKey("%" + key + "%", fromType, endType, 1);
        } catch (Exception e) {
            e.printStackTrace();
            throw new WeRunException(ExceptionsEnum.DIARYNODATA);
        }
        return result;
    }

    public int countWithAno(String uid, int fromType, int endType, int status, int ano) throws Exception {
        int result;
        try {
            result = diaryDao.countWithAno(uid, fromType, endType, status, ano);
        } catch (Exception e) {
            e.printStackTrace();
            throw new WeRunException(ExceptionsEnum.DIARYNODATA);
        }
        return result;
    }

    //根据用户id查找自己的所有
    public List<DiaryEntity> selectDiary(String uid, int type, int ano, PageInfo pageInfo) throws Exception {
        List<DiaryEntity> diarys;
        try {
            diarys = diaryDao.selectDiaryByTime(uid, type, ano, pageInfo.getFromIndex(), pageInfo.getPageSize());
            if (diarys != null)
                for (DiaryEntity d : diarys)
                    if ("default.jpg".equals(d.getDiaryImgPreview()))
                        d.setDiaryImgPreview(null);
                    else
                        d.setDiaryImgPreview(postPath + d.getDiaryImgPreview());
        } catch (Exception e) {
            e.printStackTrace();
            throw new WeRunException(ExceptionsEnum.DIARYNODATA);
        }
        return diarys;
    }

    //详细信息
    public DiaryUserVO detail(String did, UserEntity user) throws Exception {
        DiaryEntity diary = selectByDid(did);
        if (diary == null)
            throw new WeRunException(ExceptionsEnum.DIARYNODATA);
        DiaryUserVO duv = new DiaryUserVO(diary);
        if (diary.getDiaryUid().equals(user.getuId())) {
            duv.setUser(user);
            duv.setBuser(userServ.getByUid(diary.getDiaryBUid()));
            duv.setRepositity(1);
        } else if (diary.getDiaryBUid().equals(user.getuId())) {
            duv.setBuser(user);
            duv.setUser(userServ.getByUid(diary.getDiaryUid()));
            duv.setRepositity(2);
        } else {
            duv.setUser(userServ.getByUid(diary.getDiaryUid()));
            duv.setBuser(userServ.getByUid(diary.getDiaryBUid()));
            duv.setRepositity(3);
        }
        List<String> strings = annexServ.selectAnnexPath(duv.getDiaryId(), 2);
        duv.setImg(strings);
        if (strings == null)
            duv.setImg(new ArrayList<>());
        LikeEntity likeEntity = likeServ.selectByLidAndUid(did, user.getuId());
        if (likeEntity == null || likeEntity.getLikeStatus() == 2) {
            duv.setIsLike(0);
        } else {
            duv.setIsLike(1);
        }
        read(did);
        return duv;
    }

    //圈子推荐
    public List<DiaryEntity> cicleRecommand(int type1, int type2, int status, PageInfo page) throws Exception {
        List<DiaryEntity> ds = new ArrayList<>();
        try {
            ds = diaryDao.cicleRecommand(type1, type2, status, page.getFromIndex(), page.getPageSize());
            if (ds != null)
                for (DiaryEntity d : ds)
                    d.setDiaryImgPreview(postPath + d.getDiaryImgPreview());
        } catch (Exception e) {
            e.printStackTrace();
            throw new WeRunException(ExceptionsEnum.DIARYNODATA);
        }
        return ds;
    }

    //圈子热度
    public List<DiaryEntity> cicleHot(int type1, int type2, int status, PageInfo page) throws Exception {
        List<DiaryEntity> ds = new ArrayList<>();
        try {
            ds = diaryDao.cicleHot(type1, type2, status, page.getFromIndex(), page.getPageSize());
            if (ds != null)
                for (DiaryEntity d : ds)
                    d.setDiaryImgPreview(postPath + d.getDiaryImgPreview());
        } catch (Exception e) {
            e.printStackTrace();
            throw new WeRunException(ExceptionsEnum.DIARYNODATA);
        }
        return ds;
    }

    public int countRecAndHot(int type1, int type2, int status) throws Exception {
        try {
            return diaryDao.countCicleHot(type1, type2, status);
        } catch (Exception e) {
            e.printStackTrace();
            throw new WeRunException(ExceptionsEnum.DIARYNODATA);
        }
    }

    //圈子同校
    public List<DiaryEntity> cicleSameSchool(String school, int type1, int type2, int status, PageInfo page) throws Exception {
        List<DiaryEntity> ds = new ArrayList<>();
        try {
            ds = diaryDao.cicleSameSchool(school, type1, type2, status, page.getFromIndex(), page.getPageSize());
            if (ds != null)
                for (DiaryEntity d : ds)
                    d.setDiaryImgPreview(postPath + d.getDiaryImgPreview());
        } catch (Exception e) {
            e.printStackTrace();
            throw new WeRunException(ExceptionsEnum.DIARYNODATA);
        }
        return ds;
    }
    public int countCicleSchool(String uid,int type1, int type2, int status) throws Exception {
        UserSchool userSchoolByUid = userdao.getUserSchoolByUid(uid);
        if(userSchoolByUid==null)
            throw new WeRunException(ExceptionsEnum.DIARYNODATA);
        try {
            return diaryDao.countCicleSchool(userSchoolByUid.getuSchool(),type1,type2,status);
        } catch (Exception e) {
            e.printStackTrace();
            throw new WeRunException(ExceptionsEnum.DIARYNODATA);
        }
    }
    //圈子
    public List<DiaryUserVO> cicle(String uid,int type1, int type2, int status, PageInfo page, int type) throws Exception {
        List<DiaryEntity> diarys;
        List<DiaryUserVO> data = new ArrayList<>();
        try {
            switch (type) {
                case 1:
                    diarys = cicleRecommand(type1, type2, status, page);
                    break;
                case 2:
                    UserSchool userSchoolByUid = userdao.getUserSchoolByUid(uid);
                    diarys = cicleSameSchool(userSchoolByUid.getuSchool(), type1, type2, status, page);
                    break;
                case 3:
                    diarys = cicleHot(type1, type2, status, page);
                    break;
                default:
                    diarys = new ArrayList<>();
                    break;
            }
            for (DiaryEntity d : diarys) {
                data.add(new DiaryUserVO(d));
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new WeRunException(ExceptionsEnum.DIARYNODATA);
        }
        //查user
        try {
            for (DiaryUserVO duv : data) {
                duv.setUser(userServ.getByUid(duv.getDiaryUid()));
                duv.setBuser(null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new WeRunException(ExceptionsEnum.DIARYNODATA);
        }

        //annex
        try {
            for (DiaryUserVO duv : data) {
                duv.setImg(annexServ.selectAnnexPath(duv.getDiaryId(), 2));
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new WeRunException(ExceptionsEnum.DIARYNODATA);
        }
        //查询点赞关系
        for (DiaryUserVO duv : data) {
            LikeEntity likeEntity = likeServ.selectByLidAndUid(duv.getDiaryId(), uid);
            if (likeEntity == null || likeEntity.getLikeStatus() == 2) {
                duv.setIsLike(0);
            } else {
                duv.setIsLike(1);
            }
        }
        return data;

    }

    public List<DiaryUserVO> darenInfo(String uid,int type,PageInfo page)throws Exception{
        List<DiaryUserVO> duvs=new ArrayList<>();
        try {
            List<DiaryEntity> diaryEntities = diaryDao.selectByUid(uid, type, page.getFromIndex(), page.getPageSize(), 1);
            if(diaryEntities!=null)
                for(DiaryEntity d:diaryEntities)
                    duvs.add(new DiaryUserVO(d));
        }catch (Exception e){
            e.printStackTrace();
            throw new WeRunException(ExceptionsEnum.NOUSERDATA);
        }
        UserEntity byUid = userServ.getByUid(uid);
        for(DiaryUserVO duv:duvs){
            duv.setBuser(byUid);
        }
        return duvs;
    }
    public int countForDaren(String uid,int type)throws Exception{
        try {
           return diaryDao.countDaren(uid,type,1);
        }catch (Exception e){
            e.printStackTrace();
            throw new WeRunException(ExceptionsEnum.NODATA);
        }
    }

    public int changeStatus(String did,String uid)throws Exception{
        try {
            DiaryEntity diaryEntity = selectByDid(did);
            if(diaryEntity==null)
                return 0;
            if(!diaryEntity.getDiaryUid().equals(uid))
                return 0;
            return diaryDao.changeStatus(did,2);
        }catch (Exception e){
            e.printStackTrace();
            throw new WeRunException(ExceptionsEnum.ADDINSERTFAILED);
        }

    }

}

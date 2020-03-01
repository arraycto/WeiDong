package com.werun.back.controllor;

import com.werun.back.VO.DataVO;
import com.werun.back.VO.DiaryUserVO;
import com.werun.back.entity.DiaryEntity;
import com.werun.back.entity.PageInfo;
import com.werun.back.entity.UserEntity;
import com.werun.back.enums.ExceptionsEnum;
import com.werun.back.exception.WeRunException;
import com.werun.back.service.AnnexServ;
import com.werun.back.service.DiaryServ;
import com.werun.back.service.UserServ;
import com.werun.back.utils.Result;
import com.werun.back.utils.StrUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.net.URLDecoder;
import java.util.List;

/**
 * @ClassName diaryCon
 * @Author HWG
 * @Time 2019/4/21 16:17
 */
@Api(tags = {"动态、文章、问题、咨询、日记"})
@Controller
@CrossOrigin
@RequestMapping("/diary")
public class diaryCon {
    @Autowired
    private DiaryServ diaryServ;
    @Autowired
    private UserServ userServ;
    @Autowired
    AnnexServ annexServ;


    @PostMapping("/new")
    @ApiOperation(value = "插入[动态、资讯、问题、咨询、日记]", notes = "type 1动态 2咨询 3问题 4文章 5日记")
    @ResponseBody
    public DataVO insert(@RequestParam(value = "token", required = true) String token,
                         @RequestParam(value = "type", required = false, defaultValue = "1") int type,
                         @RequestParam(value = "buid", required = false, defaultValue = "default") String buid,
                         @RequestParam(value = "title", required = false, defaultValue = "快来围观我") String title,
                         @RequestParam(value = "content", required = false, defaultValue = "欢迎查看我的新动态哦！") String content,
                         @RequestParam(value = "pre_content", required = false, defaultValue = "我的新动态") String preCon,
                         @RequestParam(value = "label", required = false, defaultValue = "#跑步#打卡#") String label,
                         @RequestParam(value = "img", required = false, defaultValue = "default.jpg") String img,
                         @RequestParam(value = "ano", required = false, defaultValue = "0") int anonymous,
                         @RequestParam(value = "annex", required = false, defaultValue = "") String annex1) throws Exception {
        String annex = URLDecoder.decode(annex1, "utf-8");
//        System.out.println("annex=="+annex+"----annex1="+annex1);
//        System.out.println("decode:"+new String(annex1.getBytes("GBK"),"utf-8"));
//        System.out.println("urlDecoder"+URLDecoder.decode(annex1,"utf-8"));
        //检查是否登录
        UserEntity user = userServ.getUserInRedis(token);
        //生成基本信息
        DiaryEntity diary = new DiaryEntity();
        diary.setDiaryId(StrUtils.timeStamp() + StrUtils.randomNum(false, 5));
        diary.setDiaryUid(user.getuId());
        diary.setDiaryLable(label);
        diary.setDiaryType(type);
        diary.setDiaryBUid(buid);
        diary.setDiaryTitle(title);
        diary.setDiaryContent(content);
        diary.setDiaryContentPreview(preCon);
        diary.setDiaryImgPreview(img);
        diary.setDiaryAnonymous(anonymous);
        //写入数据库
        int insert = diaryServ.insert(diary);
        int insert1 = annexServ.insert(annex, diary.getDiaryId());
        return Result.successToArray(insert);
    }


    @PostMapping("/list")
    @ApiOperation(value = "综合查询列表")
    @ResponseBody
    public DataVO list(@RequestParam(value = "token", required = true) String token,
                       @RequestParam(value = "type", required = true, defaultValue = "1") int type,
                       @RequestParam(value = "pageNum", required = false, defaultValue = "1") int currntPage,
                       @RequestParam(value = "pageSize", required = false, defaultValue = "5") int pageSize,
                       @RequestParam(value = "order", required = false, defaultValue = "1") int order) throws Exception {
        //检查是否登录
        UserEntity user = userServ.getUserInRedis(token);
        //查询列表分页
        int count;
        switch (type) {
            case 1:
            case 2:
            case 3:
            case 4:
            case 6:
                count = diaryServ.count("%", type, type, 1);
                break;
            case 5:
                count = diaryServ.count(user.getuId(), type, type, 1);
                break;
            default:
                throw new WeRunException(ExceptionsEnum.PARAMWRONG);
        }
        //生成分页信息
        PageInfo pageInfo = new PageInfo(count, currntPage, pageSize);
        //读取数据
        List<DiaryUserVO> diaryEntities;
        if (order == 1) {      //根据时间顺序
            if (type == 5)
                diaryEntities = diaryServ.selectByTime(user.getuId(), type, type, pageInfo);
            else
                diaryEntities = diaryServ.selectByTime("%", type, type, pageInfo);
        } else {             //根据热度顺序
            if (type == 5)
                diaryEntities = diaryServ.selectByTime(user.getuId(), type, type, pageInfo);
            else
                diaryEntities = diaryServ.selectByHotDegree(pageInfo, type);
        }
        return Result.success(diaryEntities, pageInfo);
    }

//
//    @PostMapping("/listLatestDT")
//    @ApiOperation(value = "最新动态列表")
//    @ResponseBody
//    public DataVO listLatest(@RequestParam(value = "token", required = true) String token,
//                       @RequestParam(value = "pageNum", required = false, defaultValue = "1") int currntPage,
//                       @RequestParam(value = "pageSize", required = false, defaultValue = "5") int pageSize) throws Exception {
//        //检查是否登录
//        UserEntity user = userServ.getUserInRedis(token);
//        //查询列表分页
//        int count=diaryServ.count("%",1,1,1);
//        //生成分页信息
//        PageInfo pageInfo=new PageInfo(count,currntPage,pageSize);
//        //读取数据
//        List<DiaryUserVO> diaryEntities = diaryServ.selectByTime("%",1,1,pageInfo);
//        return Result.success(diaryEntities,pageInfo);
//    }

//
//    @PostMapping("/listHottestDT")
//    @ApiOperation(value = "最热动态列表")
//    @ResponseBody
//    public DataVO listHottest(@RequestParam(value = "token", required = true) String token,
//                             @RequestParam(value = "pageNum", required = false, defaultValue = "1") int currntPage,
//                             @RequestParam(value = "pageSize", required = false, defaultValue = "5") int pageSize) throws Exception {
//        //检查是否登录
//        UserEntity user = userServ.getUserInRedis(token);
//        //查询列表分页
//        int count;
//        count=diaryServ.count("%",1,1,1);
//        //生成分页信息
//        PageInfo pageInfo=new PageInfo(count,currntPage,pageSize);
//        //读取数据
//        List<DiaryUserVO> diaryEntities = diaryServ.selectByHotDegree(pageInfo,1);
//        return Result.success(diaryEntities,pageInfo);
//    }


    @PostMapping("/listDiary")
    @ApiOperation(value = "日记列表")
    @ResponseBody
    public DataVO listDiary(@RequestParam(value = "token", required = true) String token,
                            @RequestParam(value = "pageNum", required = false, defaultValue = "1") int currntPage,
                            @RequestParam(value = "pageSize", required = false, defaultValue = "5") int pageSize,
                            @RequestParam(value = "ano", required = false, defaultValue = "0") int ano) throws Exception {
        //检查是否登录
        UserEntity user = userServ.getUserInRedis(token);
        //查询列表分页
        int count;
        count = diaryServ.countWithAno(user.getuId(), 5, 5, 1, ano);
        //生成分页信息
        PageInfo pageInfo = new PageInfo(count, currntPage, pageSize);
        //读取数据
        List<DiaryEntity> diaryEntities = diaryServ.selectDiary(user.getuId(), 5, ano, pageInfo);
        return Result.success(diaryEntities, pageInfo);
    }
//
//    @PostMapping("/listLatestZX")
//    @ApiOperation(value = "最新资讯列表")
//    @ResponseBody
//    public DataVO listLatestZX(@RequestParam(value = "token", required = true) String token,
//                             @RequestParam(value = "pageNum", required = false, defaultValue = "1") int currntPage,
//                             @RequestParam(value = "pageSize", required = false, defaultValue = "5") int pageSize) throws Exception {
//        //检查是否登录
//        UserEntity user = userServ.getUserInRedis(token);
//        //查询列表分页
//        int count=diaryServ.count("%",2,2,1);
//        //生成分页信息
//        PageInfo pageInfo=new PageInfo(count,currntPage,pageSize);
//        //读取数据
//        List<DiaryUserVO> diaryEntities = diaryServ.selectByTime("%",2,2,pageInfo);
//        return Result.success(diaryEntities,pageInfo);
//    }
//
//
//    @PostMapping("/listHottestZX")
//    @ApiOperation(value = "最热资讯列表")
//    @ResponseBody
//    public DataVO listHottestZX(@RequestParam(value = "token", required = true) String token,
//                               @RequestParam(value = "pageNum", required = false, defaultValue = "1") int currntPage,
//                               @RequestParam(value = "pageSize", required = false, defaultValue = "5") int pageSize) throws Exception {
//        //检查是否登录
//        UserEntity user = userServ.getUserInRedis(token);
//        //查询列表分页
//        int count=diaryServ.count("%",2,2,1);
//        //生成分页信息
//        PageInfo pageInfo=new PageInfo(count,currntPage,pageSize);
//        //读取数据
//        List<DiaryUserVO> diaryEntities = diaryServ.selectByHotDegree(pageInfo,2);
//        return Result.success(diaryEntities,pageInfo);
//    }

    @PostMapping("/info")
    @ApiOperation(value = "DIARY详细信息")
    @ResponseBody
    public DataVO info(@RequestParam(value = "token", required = true) String token,
                       @RequestParam(value = "did", required = true) String did) throws Exception {
        //检查是否登录
        UserEntity user = userServ.getUserInRedis(token);
        DiaryUserVO detail = diaryServ.detail(did, user);
        return Result.successToArray(detail);
    }

    @PostMapping("/search")
    @ApiOperation(value = "搜索")
    @ResponseBody
    public DataVO search(@RequestParam(value = "token", required = true) String token,
                         @RequestParam(value = "key", required = true) String key,
                         @RequestParam(value = "type", required = false, defaultValue = "1") int type,
                         @RequestParam(value = "pageNum", required = false, defaultValue = "1") int pageNum,
                         @RequestParam(value = "pageSize", required = false, defaultValue = "5") int pageSize) throws Exception {

        //检查是否登录
        UserEntity user = userServ.getUserInRedis(token);
        int i = diaryServ.countKey(key, type, type);
        PageInfo pageInfo = new PageInfo(i, pageNum, pageSize);

        List<DiaryUserVO> search = diaryServ.search(key, type, pageInfo);
        return Result.success(search, pageInfo);
    }

    @PostMapping("/cicle")
    @ApiOperation(value = "DIARY详细信息")
    @ResponseBody
    public DataVO cicle(@RequestParam(value = "token", required = true) String token,
                        @RequestParam(value = "type", required = false, defaultValue = "1") int type,
                        @RequestParam(value = "pageNum", required = false, defaultValue = "1") int pageNum,
                        @RequestParam(value = "pageSize", required = false, defaultValue = "5") int pageSize) throws Exception {

        //检查是否登录
        UserEntity user = userServ.getUserInRedis(token);
        PageInfo page;
        List<DiaryUserVO> diarys;
        switch (type) {
            case 1:
                page=new PageInfo(diaryServ.countRecAndHot(1,5,1),pageNum,pageSize);
                break;
            case 2:
                page=new PageInfo(diaryServ.countCicleSchool(user.getuId(),1,5,1),pageNum,pageSize);
                break;
            case 3:
                page=new PageInfo(diaryServ.countRecAndHot(1,5,1),pageNum,pageSize);
                break;
            default:
                throw new WeRunException(ExceptionsEnum.DIARYNODATA);
        }
        diarys=diaryServ.cicle(user.getuId(),1,5,1,page,type);
        return Result.success(diarys, page);
    }

    @PostMapping("/darenReply")
    @ResponseBody
    public DataVO r(@RequestParam(value = "token", required = true) String token,
                    @RequestParam(value = "uid", required = true) String uid,
                    @RequestParam(value = "type", required = false, defaultValue = "2") int type,
                    @RequestParam(value = "pageNum", required = false, defaultValue = "1") int pageNum,
                    @RequestParam(value = "pageSize", required = false, defaultValue = "5") int pageSize)throws Exception{
        UserEntity user = userServ.getUserInRedis(token);
        int i = diaryServ.countForDaren(uid, type);
        PageInfo pageInfo = new PageInfo(i, pageNum, pageSize);
        List<DiaryUserVO> diaryUserVOS = diaryServ.darenInfo(uid, type, pageInfo);
        return Result.success(diaryUserVOS,pageInfo);

    }
    @PostMapping("/changeStatus")
    @ResponseBody
    public DataVO changeStatus(@RequestParam(value = "token", required = true) String token,
                    @RequestParam(value = "did", required = true) String did)throws Exception{
        UserEntity user = userServ.getUserInRedis(token);
        int i = diaryServ.changeStatus(did, user.getuId());
        if(i==0)
            throw new WeRunException(ExceptionsEnum.NOAUTHORITY);
        return Result.successToArray(i);
    }

}

package com.werun.back.utils;


import com.alibaba.fastjson.JSONObject;
import com.google.gson.JsonObject;
import com.werun.back.VO.DataVO;
import com.werun.back.entity.PageInfo;
import com.werun.back.enums.ExceptionsEnum;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName Result
 * @Author HWG
 * @Time 2019/4/17 14:34
 */

public class Result {

    public static DataVO success(Object object){
        try{
            return generateDataVO(object, ExceptionsEnum.SUCCESS,null);
        }catch (Exception e){
            throw e;
        }
    }
    public static DataVO successToArray(Object object){
        List<Object> l=new ArrayList<>();
        try{
            l.add(object);
            return generateDataVO(l, ExceptionsEnum.SUCCESS,null);
        }catch (Exception e){
            throw e;
        }
    }
    public static DataVO successToArray(String key,Object object){
        try{
            List<Object> d=new ArrayList<>();
            JSONObject jsonObject=new JSONObject();
            jsonObject.put(key,object);
            d.add(jsonObject);
            return generateDataVO(d, ExceptionsEnum.SUCCESS,null);
        }catch (Exception e){
            throw e;
        }
    }
    public static DataVO success(Object object,PageInfo pageInfo){
        try{
            return generateDataVO(object, ExceptionsEnum.SUCCESS,pageInfo);
        }catch (Exception e){
            throw e;
        }
    }
    //默认成功信息
    public static DataVO success(){
        try{
            return generateDataVO(null, ExceptionsEnum.SUCCESS,null);
        }catch (Exception e){
            throw e;
        }
    }
    //结果信息  信息性质
    public static DataVO result(ExceptionsEnum exceptionsEnum){
        try{
            return generateDataVO(null,exceptionsEnum,null);
        }catch (Exception e){
            throw e;
        }
    }
    //默认错误信息，无data
    public static DataVO error(){
        try{
            return generateDataVO(null,ExceptionsEnum.ERROR,null);
        }catch (Exception e){
            throw e;
        }
    }
    //错误信息，有data  默认错误代码
    public static DataVO error(Object object){
        try{
            return generateDataVO(object,ExceptionsEnum.ERROR,null);
        }catch (Exception e){
            throw e;
        }
    }
    //错误信息  带data
    public static DataVO error(Object object,ExceptionsEnum exceptionsEnum){
        try{
            return generateDataVO(object,exceptionsEnum,null);
        }catch (Exception e){
            throw e;
        }
    }
    //错误信息  无data
    public static DataVO error(ExceptionsEnum exceptionsEnum){
        try{
            return generateDataVO(null,exceptionsEnum,null);
        }catch (Exception e){
            throw e;
        }
    }

    public static DataVO generateDataVO(Object object, ExceptionsEnum exceptionsEnum,PageInfo pageInfo){
        DataVO dataVO=new DataVO();
        dataVO.setCode(exceptionsEnum.getCode());
        dataVO.setMsg(exceptionsEnum.getMessage());
        if(pageInfo==null)
            dataVO.setPageInfo(new PageInfo());
        else
            dataVO.setPageInfo(pageInfo);
        if(object==null)
            dataVO.setData(new ArrayList<>());
        else
            dataVO.setData(object);
        return dataVO;
    }
}

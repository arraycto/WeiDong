package com.werun.back.service;

import com.werun.back.enums.ExceptionsEnum;
import com.werun.back.exception.WeRunException;
import com.werun.back.utils.StrUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

/**
 * @ClassName fileServ
 * @Author HWG
 * @Time 2019/4/17 21:25
 */
@Service
public class fileServ {

    @Value("${FileOp.upload.path}")
    private String filePath;
    @Value("${FileOp.upload.headPath}")
    private String headPath;
    @Value("${FileOp.upload.goodPath}")
    private String goodPath;
    @Value("${FileOp.upload.postPath}")
    private String postPath;

    public String upoad(MultipartFile file,int type)throws Exception{
        if(file.isEmpty())
            throw new WeRunException(ExceptionsEnum.NOFILESELECT);
        if(file.getSize()>5242880)
            throw new WeRunException(ExceptionsEnum.FILETOOBIG);
        //重命名
        String filename = file.getOriginalFilename();
        String suffixName=filename.substring(filename.lastIndexOf("."));
        String rename= StrUtils.timeStamp()+ StrUtils.randomNum(true,3)+suffixName;
        String serverPath="";
        switch (type){
            case 1:
                serverPath=headPath+rename;break;
            case 2:
                serverPath=postPath+rename;break;
            case 3:
                serverPath=goodPath+rename;break;
            default:
                serverPath=filePath+rename;break;
        }
        try{
            File dest=new File(serverPath);
            file.transferTo(dest);
            return rename;
        }catch (Exception e)
        {
            e.printStackTrace();
            throw new WeRunException(ExceptionsEnum.UPLOADFAILED);
        }
    }


}

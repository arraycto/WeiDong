package com.werun.back.service;

import com.werun.back.VO.DataVO;
import com.werun.back.enums.ExceptionsEnum;
import com.werun.back.exception.WeRunException;
import org.springframework.stereotype.Service;

/**
 * @ClassName testServ
 * @Author HWG
 * @Time 2019/4/17 15:12
 */
@Service
public class testServ {

    public DataVO extest(int i) throws Exception{
        switch (i){
            case 1:
                throw new WeRunException(ExceptionsEnum.SUCCESS);
            case 2:
                throw new WeRunException(ExceptionsEnum.ERROR);
            default:
                throw new WeRunException(ExceptionsEnum.ERROR);
        }
    }
}

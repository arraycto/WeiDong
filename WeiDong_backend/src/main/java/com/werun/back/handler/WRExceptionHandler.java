package com.werun.back.handler;

import com.werun.back.VO.DataVO;
import com.werun.back.enums.ExceptionsEnum;
import com.werun.back.exception.WeRunException;
import com.werun.back.utils.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @ClassName ExceptionHandler
 * @Author HWG
 * @Time 2019/4/17 14:57
 */
@ControllerAdvice
public class WRExceptionHandler {
    private final static Logger logger= LoggerFactory.getLogger(WRExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public DataVO exceptionHandle(Exception e){
        if(e instanceof WeRunException){
            logger.info(e.getMessage());
            WeRunException exception=(WeRunException)e;
            return Result.error(null,ExceptionsEnum.getValue(((WeRunException) e).getCode()));
        }else {
            e.printStackTrace();
            return Result.error();
        }
    }
}

package com.werun.back.controllor;

import com.werun.back.VO.DataVO;
import com.werun.back.entity.TokenUserVO;
import com.werun.back.enums.ExceptionsEnum;
import com.werun.back.exception.WeRunException;
import com.werun.back.service.SmsServ;
import com.werun.back.service.fileServ;
import com.werun.back.service.testServ;
import com.werun.back.token.RTM;
import com.werun.back.utils.Result;
import com.werun.back.utils.StrUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.util.WebUtils;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

@Api(value = "测试用API",tags = {"测试专用接口"})
@Controller
@CrossOrigin
@RequestMapping("/test")
public class testCon {

    @Autowired
    private testServ testserv;
    @Autowired
    private fileServ fileserv;
    @Autowired
    private SmsServ smsServ;
    @Autowired
    private RTM rtm;

    @ApiOperation("测试用")
    @PostMapping("/test")
    @ResponseBody
    @ApiIgnore
    public DataVO test(@RequestParam(value = "param1",required = false,defaultValue = "1")String param1) throws Exception{

        return testserv.extest(Integer.parseInt(param1));
    }
    @ApiOperation("上传文件")
    @PostMapping("/fileUpload")
    @ResponseBody
//    @ApiIgnore
    public DataVO upload(HttpServletRequest request,int type)throws Exception{
        String contentType = request.getContentType();
        if(contentType!=null&&contentType.toLowerCase().startsWith("multipart/")){
            MultipartHttpServletRequest multipartReqeust= WebUtils.getNativeRequest(request,MultipartHttpServletRequest.class);
            Map<String, MultipartFile> fileMap = multipartReqeust.getFileMap();
//            Set<String> names = fileMap.keySet();
//            for(String s: names){
//                System.out.print(s);
//                System.out.println("--+"+fileMap.get(s).getOriginalFilename());
//            }
            Collection<MultipartFile> values = fileMap.values();
//            int type = Integer.parseInt(request.getParameter("type"));
//            String type1 = multipartReqeust.getParameter("type");
//            int type2 = Integer.parseInt(String.valueOf(request.getAttribute("type")));
//            System.out.println("type:"+type+"----type1:");
            StringBuffer sb=new StringBuffer();
            for(MultipartFile file:values){
                String upoad = fileserv.upoad(file, type);
                sb.append(upoad).append(",");
            }
//            System.out.println(sb.toString());
            return Result.successToArray(sb.toString());
        }
        return Result.error();
    }
    @ApiOperation("头像上传")
    @PostMapping("/headUpload")
    @ResponseBody
    public DataVO headUpload(MultipartFile file)throws Exception{
        return Result.successToArray(fileserv.upoad(file,1));
    }
    @ApiOperation("动态图片上传")
    @PostMapping("/postUpload")
    @ResponseBody
    public DataVO postUpload(MultipartFile file)throws Exception{
        return Result.successToArray(fileserv.upoad(file,2));
    }
    @ApiOperation("商品图片上传")
    @PostMapping("/goodUpload")
    @ResponseBody
    public DataVO goodUpload(MultipartFile file)throws Exception{
        return Result.successToArray(fileserv.upoad(file,3));
    }

    @RequestMapping("/upload")
    public String upload2(){

        return "upload";
    }

    @GetMapping("/redirect")
    @ResponseBody
    public DataVO redirect()throws Exception{
        throw new WeRunException(ExceptionsEnum.NOLOGIN);
    }
}

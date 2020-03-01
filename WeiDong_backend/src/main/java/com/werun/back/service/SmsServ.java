package com.werun.back.service;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.werun.back.entity.UserEntity;
import com.werun.back.enums.ExceptionsEnum;
import com.werun.back.exception.WeRunException;
import com.werun.back.utils.StrUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * @ClassName SmsServ
 * @Author HWG
 * @Time 2019/4/18 14:03
 */
@Service
public class SmsServ {

    @Autowired
    private StringRedisTemplate template;
    @Autowired
    @Qualifier("userRedisTemplate")
    private RedisTemplate<String, UserEntity> urt;
    @Autowired
    private UserServ userServ;

    public ExceptionsEnum sendCode(String phone, String code){
        //设置超时时间-可自行调整
        System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
        System.setProperty("sun.net.client.defaultReadTimeout", "10000");
        //初始化ascClient需要的几个参数
        final String product = "Dysmsapi";//短信API产品名称（短信产品名固定，无需修改）
        final String domain = "dysmsapi.aliyuncs.com";//短信API产品域名（接口地址固定，无需修改）
        //HWG阿里云
        final String accessKeyId = "LTAI8jr70gyVS8Xw";//你的accessKeyId,参考本文档步骤2
        final String accessKeySecret = "VncWlz7rY6zrhrRkWusqgfxOSu5mHO";//你的accessKeySecret，参考本文档步骤2
        //陈老板阿里云
//        final String accessKeyId = "LTAIt2OMF0RdFsw0";//你的accessKeyId,参考本文档步骤2
//        final String accessKeySecret = "CKNdqWQgzmYmPdeZetwpu0HmXHmEQH";//你的accessKeySecret，参考本文档步骤2
        //胡老板阿里云
//        final String accessKeyId = "LTAIw3nyEeBYOsLZ";//你的accessKeyId,参考本文档步骤2
//        final String accessKeySecret = "quGdeG3F7a69JpwLknpKsGxzQGaJBA";//你的accessKeySecret，参考本文档步骤2
        //初始化ascClient,暂时不支持多region（请勿修改）
        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId,
                accessKeySecret);
        try {
            DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product, domain);
        } catch (ClientException e) {
            e.printStackTrace();
        }
        IAcsClient acsClient = new DefaultAcsClient(profile);
        //组装请求对象
        SendSmsRequest request = new SendSmsRequest();
        //使用post提交
        request.setMethod(MethodType.POST);
        //必填:待发送手机号。支持以逗号分隔的形式进行批量调用，批量上限为1000个手机号码,批量调用相对于单条调用及时性稍有延迟,验证码类型的短信推荐使用单条调用的方式；发送国际/港澳台消息时，接收号码格式为国际区号+号码，如“85200000000”
        request.setPhoneNumbers(phone);
        //HWG阿里云
        request.setSignName("一点五十八");
        request.setTemplateCode("SMS_163725477");
        //陈老板阿里云
//        request.setSignName("南昌城市合伙人");
//        request.setTemplateCode("SMS_147125242");
        //胡老板阿里云
//        request.setSignName("微动");
//        request.setTemplateCode("SMS_164512807");
        //可选:模板中的变量替换JSON串,如模板内容为"亲爱的${name},您的验证码为${code}"时,此处的值为
        //友情提示:如果JSON中需要带换行符,请参照标准的JSON协议对换行符的要求,比如短信内容中包含\r\n的情况在JSON中需要表示成\\r\\n,否则会导致JSON在服务端解析失败
        request.setTemplateParam("{\"code\":\""+code+"\"}");
        //可选-上行短信扩展码(扩展码字段控制在7位或以下，无特殊需求用户请忽略此字段)
        //request.setSmsUpExtendCode("90997");
        //可选:outId为提供给业务方扩展字段,最终在短信回执消息中将此值带回给调用者
        //request.setOutId("yourOutId");
        //请求失败这里会抛ClientException异常
        SendSmsResponse sendSmsResponse = null;
        try {
            sendSmsResponse = acsClient.getAcsResponse(request);
        } catch (ClientException e) {
            e.printStackTrace();
            return ExceptionsEnum.NETWORKEXCEPTION;
        }
        if(sendSmsResponse.getCode() != null && sendSmsResponse.getCode().equals("OK")) {
            template.boundValueOps(phone).set(code,5, TimeUnit.MINUTES);
            return ExceptionsEnum.SUCCESS;
        }else if(sendSmsResponse.getCode() != null && sendSmsResponse.getCode().equals("isv.BUSINESS_LIMIT_CONTROL"))
            return ExceptionsEnum.SENDFREQUENTLY;

        return ExceptionsEnum.ERROR;
    }

    public ExceptionsEnum sendTestCode(String phone){
        template.boundValueOps(phone).set("123456",5, TimeUnit.MINUTES);
        return ExceptionsEnum.SUCCESS;
    }

    public String login(String phone,String code)throws Exception{
        String Rcode = (String) template.boundValueOps(phone).get();
        if(Rcode==null||Rcode.equals("null")||Rcode=="")
            throw new WeRunException(ExceptionsEnum.CODEINVALID);
        else if(code.equals(Rcode)){
            UserEntity u=userServ.getByPhone(phone);
            if(u==null){
                userServ.insert(phone);
            }
            u=userServ.getByPhone(phone);
            String token= StrUtils.randomNum(false,10);
//            template.boundValueOps(token).set(uid,30,TimeUnit.MINUTES);
            urt.opsForValue().set(token,u,72,TimeUnit.HOURS);
            return token;
        }else{
            throw new WeRunException(ExceptionsEnum.CODEWRONG);
        }
    }
}

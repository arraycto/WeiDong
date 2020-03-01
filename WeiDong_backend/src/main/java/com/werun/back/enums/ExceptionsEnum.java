package com.werun.back.enums;

/**
 * @ClassName ExceptionsEnum
 * @Author HWG
 * @Time 2019/4/17 14:41
 */
public enum ExceptionsEnum {
    NOLOGIN(10001, "未登录"),
    WRONGPWD(10002, "密码错误"),
    PARAMWRONG(101, "参数错误"),
    SUCCESS(0, "成功"),
    ERROR(-1, "未知错误"),
    NOFILESELECT(10003, "没有选中文件"),
    FILETOOBIG(10004, "文件过大"),
    UPLOADFAILED(10005, "上传失败"),
    NETWORKEXCEPTION(10006, "网络异常，发送失败"),
    SENDFREQUENTLY(10007, "验证码获取频繁"),
    CODEINVALID(10008, "验证码已过时"),
    REGFAILED(10009, "注册失败！写入数据库失败！"),
    CODEWRONG(10010, "验证码错误"),
    UPDATEUSERBODYFAILED(10011, "更新用户身体信息失败"),
    UPDATEUSERSCHOOLFAILED(10012, "更新用户单位信息失败"),
    INSETUSERBODYFAILED(10013, "初始化用户身体信息失败"),
    INSETUSERSCHOOLFAILED(10014, "初始化用户单位信息失败"),
    NODATA(10015, "没有相关数据"),
    GOODINSERTFAILED(10016, "商品入库失败"),
    ANNEXINSERTFAILED(10017, "附件保存失败"),
    NOANNEXPATH(10018, "没有附件路径"),
    NOGOODDATA(10019, "ID错误，没有相应数据"),
    NOUSERDATA(10020, "没有用户数据"),
    INSERTORDERFAILED(10021, "生成订单失败"),
    ORDERUPDATEFAILED(10022, "订单状态更改失败"),
    UPDATESTOCKFAILED(10023, "更新库存失败"),
    INVALIDORDER(10024, "订单号错误"),
    NOAUTHORITY(10025, "没有权限操作"),
    UPDATEGOODSTATUSFAILED(10026, "更新商品状态失败"),
    DIARYINSERTFAILED(20001, "DIARY插入失败"),
    DIARYNODATA(20002, "没有DIARY数据"),
    DIARYREADFAILED(20003, "DIARY添加阅读数量失败"),
    DIARYLIKEFAILED(20004, "DIARY添加点赞数量失败"),
    DIARYCOMMENTFAILED(20005, "DIARY添加评论数量失败"),
    ACTINSERTFAILED(30001, "活动入库失败！"),
    ACTWRONGAID(30002, "参数错误，没有相应数据"),
    ACTJOINFAILED(30003, "参加失败"),
    ACTOUTFAILED(30004, "退出失败"),
    ACTCHANGEFAILED(30005, "活动状态更改失败"),
    LIKEINSERTFAILED(30006, "点赞失败"),
    LIKEWRONGPARAM(30007, "参数错误"),
    LIKENODATA(30008, "没有相关数据"),
    LIKEUPDATEFAILED(30009, "更改点赞失败"),
    LIKELIKED(30010, "已经点赞或踩过"),
    COMINSERTFAILED(40001, "评论入库失败"),
    COMNODATA(40002, "无评论"),
    EXINSERTFAILD(50001, "运动数据保存失败"),
    EXNODATA(60001, "没有运动数据"),
    ADDNUMTOOMUCH(60002, "地址数不能多过5条"),
    ADDNOADD(60003, "没有绑定地址"),
    ADDINSERTFAILED(60004, "保存失败！请重试"),
    ADDNOAUTHORY(60005, "没有权限操作"),
    ADDSETDEFAILED(60006, "设置默认地址失败，请重试"),
    ;

    private Integer code;
    private String message;

    ExceptionsEnum(Integer code, String msg) {
        this.code = code;
        this.message = msg;
    }

    public Integer getCode() {
        return code;
    }


    public String getMessage() {
        return message;
    }

    public static ExceptionsEnum getValue(int code) {
        for (ExceptionsEnum exceptionsEnum : values()) {
            if (exceptionsEnum.getCode() == code) {
                return exceptionsEnum;
            }
        }
        return ExceptionsEnum.ERROR;
    }
}

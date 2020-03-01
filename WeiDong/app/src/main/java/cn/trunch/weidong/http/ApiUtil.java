package cn.trunch.weidong.http;


public class ApiUtil {
    public static String USER_ID = ""; // 用户ID
    public static String USER_TOKEN = ""; // 用户TOKEN
    public static String USER_AVATAR = ""; // 用户头像
    public static String USER_NAME = ""; // 用户昵称
    public final static int thresholdS = 5;
    //--------------------用户常用基本信息--------------
    public final static String AK = "Iz1ipT6hAI3TVkeCroQLWXVKHmkOGTom";
    public final static String service_id = "212124";
    public final static String IP_PORT = "http://www.two2two.xyz/back"; // IP+端口号
    //    private final static String IP_PORT = "http://192.168.1.107:8080/back"; // IP+端口号
//    private final static String IP_PORT = "http://10.0.2.2:8080/back"; // IP+端口号
    private final static String MAP_API_DIS = "http://yingyan.baidu.com/api/v3/track/getdistance"; //历程查询api
    public final static String DIARY_LIST = IP_PORT + "/diary/list";
    public final static String DIARY_SEARCH = IP_PORT + "/diary/search";
    public final static String DIARY_CICLE = IP_PORT + "/diary/cicle";
    public final static String DIARY_DIARY_LIST = IP_PORT + "/diary/listDiary";
    public final static Integer POST_TYPE = 1; //动态
    public final static Integer ARTICLE_TYPE = 4; //资讯
    public final static Integer QUESTION_TYPE = 3; //问题
    public final static Integer CONSULT_TYPE = 2;   //咨询
    public final static Integer DIARY_TYPE = 5; //日记
    public final static String USER_LOGIN = IP_PORT + "/user/login";
    public final static String USER_SENDSMS = IP_PORT + "/user/sendSms";
    public final static String USER_INFO = IP_PORT + "/user/userInfo";
    public final static String USER_BODY_INFO = IP_PORT + "/user/userBodyInfo";
    public final static String USER_SCHOOL_INFO = IP_PORT + "/user/userSchoolInfo";
    public final static String USER_UPDATE = IP_PORT + "/user/updateUser";
    public final static String USER_SCHOOL_UPDATE = IP_PORT + "/user/updateUSchool";
    public final static String USER_EXPERT = IP_PORT + "/user/daren";
    public final static String USER_EXPERT_INFO = IP_PORT + "/user/darenInfo";
    public final static String EX_LIST = IP_PORT + "/ex/list";
    public final static String EX_ADD = IP_PORT + "/ex/add";
    public final static String FILE_UPLOAD_DIARY = IP_PORT + "/test/postUpload";
    public final static String FILE_UPLOAD_HEAD = IP_PORT + "/test/headUpload";
    public final static String MULTFILE_UPLOAD = IP_PORT + "/test/fileUpload";
    public final static Integer MULT_HEAD = 1;   //头像
    public final static Integer MULT_POST = 2;   //动态
    public final static Integer MULT_GOOD = 3;   //商品
    public final static String DIARY_ADD = IP_PORT + "/diary/new";
    public final static String DIARY_INFO = IP_PORT + "/diary/info";
    public final static String DIARY_EXPERT_REPLY = IP_PORT + "/diary/darenReply";

    public final static String TEAM_lIST = IP_PORT + "/team/list";
    public final static String TEAM_INFO = IP_PORT + "/team/query";
    public final static String TEAM_OUT = IP_PORT + "/team/out";
    public final static String TEAM_JOIN = IP_PORT + "/team/join";
    public final static String TEAM_CHANGE = IP_PORT + "/team/changeStatus";
    public final static String TEAM_ADD = IP_PORT + "/team/add";
    public final static String TEAM_SEARCH = IP_PORT + "/team/search";
    public final static String COM_LIST = IP_PORT + "/comment/list";
    public final static String COM_ADD = IP_PORT + "/comment/add";
    public final static String LIKE_ADD = IP_PORT + "/like/add";
    public final static String LIKE_LIST = IP_PORT + "/like/list";
    public final static String RANK_MY = IP_PORT + "/rank/myRank";
    public final static String RANK_SCHOOL = IP_PORT + "/rank/schoolRank";

}



SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for activity
-- ----------------------------
DROP TABLE IF EXISTS `activity`;
CREATE TABLE `activity`  (
  `act_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '活动id',
  `act_uid` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '发起人',
  `act_tid` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '活动属于的小组',
  `act_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '活动名字',
  `act_desc` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '活动简介',
  `act_location` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '地点',
  `act_start_time` timestamp NULL DEFAULT NULL COMMENT '开始时间',
  `act_end_time` timestamp NULL DEFAULT NULL COMMENT '结束时间',
  `act_user_num` int(11) NULL DEFAULT 0 COMMENT '参与人数',
  `act_set_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '活动建立时间',
  `act_status` int(11) NULL DEFAULT 1 COMMENT '活动状态 1报名中 2进行中 3结束',
  `act_preview_img` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '预览图片',
  PRIMARY KEY (`act_id`) USING BTREE,
  INDEX `u8_id`(`act_uid`) USING BTREE,
  CONSTRAINT `u8_id` FOREIGN KEY (`act_uid`) REFERENCES `user` (`u_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `act_tid` FOREIGN KEY (`act_id`) REFERENCES `team` (`team_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for address
-- ----------------------------
DROP TABLE IF EXISTS `address`;
CREATE TABLE `address`  (
  `ad_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '地址id',
  `ad_uid` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户id',
  `ad_name` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '收货人姓名',
  `ad_phone` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '收货人电话',
  `ad_province` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '地址-省份',
  `ad_city` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '地址-城市',
  `ad_county` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '地区-所属区',
  `ad_dtl` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '详细地址',
  `ad_status` int(11) NULL DEFAULT 1 COMMENT '地址状态 1正常 2已失效',
  `ad_default` int(11) NULL DEFAULT 0 COMMENT '默认地址 0非默认地址 1默认地址',
  PRIMARY KEY (`ad_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for annex
-- ----------------------------
DROP TABLE IF EXISTS `annex`;
CREATE TABLE `annex`  (
  `post_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '所属父项id 如商品id 或动态id',
  `annex_name` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '文件名',
  `annex_order` int(11) NULL DEFAULT 0 COMMENT '附件顺序',
  INDEX `diary1_id`(`post_id`) USING BTREE,
  CONSTRAINT `diary1_id` FOREIGN KEY (`post_id`) REFERENCES `diary` (`diary_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for comment
-- ----------------------------
DROP TABLE IF EXISTS `comment`;
CREATE TABLE `comment`  (
  `com_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '评论id',
  `com_item_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '父项id',
  `com_uid` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '评论者id',
  `com_b_uid` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '被评论项id',
  `com_type` int(11) NULL DEFAULT 1 COMMENT '类型 1提问 2回复 3评论',
  `com_content` varchar(512) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '评论内容',
  `com_like_num` int(11) NULL DEFAULT 0 COMMENT '点赞量',
  `com_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '评论时间',
  PRIMARY KEY (`com_id`) USING BTREE,
  INDEX `com_item_id`(`com_item_id`) USING BTREE,
  INDEX `u6_id`(`com_uid`) USING BTREE,
  INDEX `u7_id`(`com_b_uid`) USING BTREE,
  CONSTRAINT `u6_id` FOREIGN KEY (`com_uid`) REFERENCES `user` (`u_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `u7_id` FOREIGN KEY (`com_b_uid`) REFERENCES `user` (`u_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `com_item_id` FOREIGN KEY (`com_item_id`) REFERENCES `diary` (`diary_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for diary
-- ----------------------------
DROP TABLE IF EXISTS `diary`;
CREATE TABLE `diary`  (
  `diary_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '动态id',
  `diary_uid` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '发布人id',
  `diary_b_uid` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '被咨询人id',
  `diary_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '发布时间',
  `diary_lable` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '动态标签',
  `diary_type` int(11) NULL DEFAULT 1 COMMENT '1动态 2资讯 3问题 4资讯 5日记',
  `diary_title` varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '标题',
  `diary_content` varchar(5021) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '内容',
  `diary_content_preview` varchar(512) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '内容预览',
  `diary_img_preview` varchar(512) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '预览图',
  `diary_anonymous` int(11) NULL DEFAULT 0 COMMENT '是否匿名 1匿名  0公开',
  `diary_read_num` int(11) NULL DEFAULT 0 COMMENT '浏览量',
  `diary_comment_num` int(11) NULL DEFAULT 0 COMMENT '评论数',
  `diary_like_num` int(11) NULL DEFAULT 0 COMMENT '点赞数',
  `diary_reward` int(16) NULL DEFAULT 0 COMMENT '打赏金额',
  `diary_status` int(11) NULL DEFAULT 1 COMMENT '状态 1正常 2已删除 3不可见',
  PRIMARY KEY (`diary_id`) USING BTREE,
  INDEX `u5_id`(`diary_uid`) USING BTREE,
  CONSTRAINT `u5_id` FOREIGN KEY (`diary_uid`) REFERENCES `user` (`u_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '相当去文章发布' ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for exercise
-- ----------------------------
DROP TABLE IF EXISTS `exercise`;
CREATE TABLE `exercise`  (
  `ex_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '运动数据id',
  `u_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户id',
  `ex_type` int(11) NULL DEFAULT 1 COMMENT '运动类型',
  `ex_amount` int(11) NULL DEFAULT 0 COMMENT '运动量',
  `ex_location_latitude` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '定位纬度',
  `ex_location_longitude` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '定位经度',
  `ex_start_time` timestamp NULL DEFAULT NULL COMMENT '开始时间',
  `ex_end_time` timestamp NULL DEFAULT NULL COMMENT '结束时间',
  PRIMARY KEY (`ex_id`) USING BTREE,
  INDEX `u4_id`(`u_id`) USING BTREE,
  CONSTRAINT `u4_id` FOREIGN KEY (`u_id`) REFERENCES `user` (`u_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for goods
-- ----------------------------
DROP TABLE IF EXISTS `goods`;
CREATE TABLE `goods`  (
  `g_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '商品id',
  `g_uid` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '发布人id',
  `g_price` int(11) NULL DEFAULT NULL COMMENT '商品现价(单位分)',
  `g_oldprice` int(11) NULL DEFAULT 0 COMMENT '商品原价(单位分)',
  `g_name` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商品名',
  `g_preview_img` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '预览图(展示大图)',
  `g_desc` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商品介绍',
  `g_stock` int(11) NULL DEFAULT 1 COMMENT '商品存货',
  `g_sale_num` int(11) NULL DEFAULT 0 COMMENT '商品销量',
  `g_type` int(11) NULL DEFAULT 10001 COMMENT '商品类型 1官方 2用户 10001衣物 10002运动器械 10003运动周边 20001用户发布出租物品',
  `g_status` int(11) NULL DEFAULT 1 COMMENT '商品状态1正常 2售罄 3下架',
  `g_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '入库时间',
  PRIMARY KEY (`g_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for likes
-- ----------------------------
DROP TABLE IF EXISTS `likes`;
CREATE TABLE `likes`  (
  `like_item_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '父项id',
  `like_uid` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '评论者id',
  `like_type` int(11) NULL DEFAULT 1 COMMENT '点赞类型 1喜欢 2不喜欢(踩)',
  `like_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '点赞时间',
  `like_status` int(11) NULL DEFAULT 1 COMMENT '状态 1正常 2取消',
  INDEX `item_id`(`like_item_id`) USING BTREE,
  INDEX `like_uid`(`like_uid`) USING BTREE,
  CONSTRAINT `item_id` FOREIGN KEY (`like_item_id`) REFERENCES `diary` (`diary_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `like_uid` FOREIGN KEY (`like_uid`) REFERENCES `user` (`u_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for orders
-- ----------------------------
DROP TABLE IF EXISTS `orders`;
CREATE TABLE `orders`  (
  `o_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '订单id',
  `o_uid` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户id',
  `o_gid` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商品id',
  `o_address` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '收获地址id',
  `o_price` int(11) NULL DEFAULT NULL COMMENT '实际付款',
  `o_num` int(11) NULL DEFAULT NULL COMMENT '商品数量',
  `o_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '下单时间',
  `o_status` int(11) NULL DEFAULT 1 COMMENT '订单状态 1未付款 2待发货 3 待收货 4带评论 5完成 6被用户删除',
  PRIMARY KEY (`o_id`) USING BTREE,
  INDEX `o_uid`(`o_uid`) USING BTREE,
  INDEX `o_gid`(`o_gid`) USING BTREE,
  INDEX `o_address`(`o_address`) USING BTREE,
  CONSTRAINT `o_uid` FOREIGN KEY (`o_uid`) REFERENCES `user` (`u_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `o_gid` FOREIGN KEY (`o_gid`) REFERENCES `goods` (`g_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `o_address` FOREIGN KEY (`o_address`) REFERENCES `address` (`ad_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for school_rank
-- ----------------------------
DROP TABLE IF EXISTS `school_rank`;
CREATE TABLE `school_rank`  (
  `s_name` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '学校全称',
  `s_stu_num` int(11) NULL DEFAULT 0 COMMENT '学生数量(认证为本校且今日打卡运动)',
  `s_ex_time` int(11) NULL DEFAULT NULL COMMENT '总运动时间',
  `s_history_num` int(11) NULL DEFAULT 0 COMMENT '历史前十次数',
  `s_update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '数据更新时间',
  INDEX `s_name`(`s_name`) USING BTREE,
  CONSTRAINT `s_name` FOREIGN KEY (`s_name`) REFERENCES `user_school` (`u_school`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for team
-- ----------------------------
DROP TABLE IF EXISTS `team`;
CREATE TABLE `team`  (
  `team_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '小组id',
  `team_uid` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '发起人id',
  `team_type` int(11) NULL DEFAULT 1 COMMENT '小组类型',
  `team_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '小组名称',
  `team_desc` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '小组简介',
  `team_location` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '小组地点(获取近似地点)',
  `team_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '小组建立时间',
  `act_user_num` int(11) NULL DEFAULT 0 COMMENT '小组人数',
  `team_status` int(11) NULL DEFAULT 1 COMMENT '状态 1正常 2解散',
  `act_avatar` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '小组头像',
  PRIMARY KEY (`team_id`) USING BTREE,
  INDEX `u3_id`(`team_uid`) USING BTREE,
  CONSTRAINT `u3_id` FOREIGN KEY (`team_uid`) REFERENCES `user` (`u_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `u_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户id',
  `u_phone` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户手机号',
  `u_pwd` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '123456' COMMENT '用户密码',
  `u_nickname` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户昵称',
  `u_rank` int(11) NULL DEFAULT 1 COMMENT '用户等级',
  `u_selfdes` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '个人介绍',
  `u_gender` int(11) NULL DEFAULT 1 COMMENT '性别: 1男 2女',
  `u_birthday` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '出生日期',
  `u_avatar` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '头像',
  `u_ex_time` int(11) NULL DEFAULT 0 COMMENT '总运动时间',
  `u_ex_amount` int(11) NULL DEFAULT 0 COMMENT '总运动路程',
  `u_reg_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '注册时间',
  `u_post_num` int(11) NULL DEFAULT 0 COMMENT '发表动态数',
  PRIMARY KEY (`u_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for user2team
-- ----------------------------
DROP TABLE IF EXISTS `user2team`;
CREATE TABLE `user2team`  (
  `t_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '小组id',
  `u_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户id',
  `type` int(11) NULL DEFAULT 1 COMMENT '1活动 2小组',
  `time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '数据生成时间',
  `status` int(11) NULL DEFAULT 1 COMMENT '用户小组关系 1正常 2退出',
  PRIMARY KEY (`t_id`, `u_id`) USING BTREE,
  INDEX `u_id`(`u_id`) USING BTREE,
  CONSTRAINT `u_id` FOREIGN KEY (`u_id`) REFERENCES `user` (`u_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `team_id` FOREIGN KEY (`t_id`) REFERENCES `team` (`team_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for user_body
-- ----------------------------
DROP TABLE IF EXISTS `user_body`;
CREATE TABLE `user_body`  (
  `u_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户id',
  `u_height` int(11) NULL DEFAULT 175 COMMENT '用户身高(单位厘米)',
  `u_weight` int(11) NULL DEFAULT 50 COMMENT '用户体重(单位千克)',
  `u_vial_cap` int(11) NULL DEFAULT 3500 COMMENT '用户肺活量(单位毫升)',
  `u_change_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`u_id`) USING BTREE,
  CONSTRAINT `u1_id` FOREIGN KEY (`u_id`) REFERENCES `user` (`u_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for user_school
-- ----------------------------
DROP TABLE IF EXISTS `user_school`;
CREATE TABLE `user_school`  (
  `u_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户id',
  `u_school` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '完整校名',
  `u_academy` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '所属学院',
  `u_number` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '学号',
  `u_reg_year` int(11) NULL DEFAULT 2016 COMMENT '入学年份',
  `u_edu` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '学历(默认本科)',
  `u_change_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `u_img` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '学生证图片',
  PRIMARY KEY (`u_id`) USING BTREE,
  INDEX `u_school`(`u_school`) USING BTREE,
  CONSTRAINT `u2_id` FOREIGN KEY (`u_id`) REFERENCES `user` (`u_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Triggers structure for table orders
-- ----------------------------
DROP TRIGGER IF EXISTS `changeStatus`;
delimiter ;;
CREATE TRIGGER `changeStatus` BEFORE UPDATE ON `orders` FOR EACH ROW begin
if new.o_status<=0
then set new.o_status=old.o_status+1;
end if;
end
;;
delimiter ;

-- ----------------------------
-- Triggers structure for table user
-- ----------------------------
DROP TRIGGER IF EXISTS `beforeUpdate`;
delimiter ;;
CREATE TRIGGER `beforeUpdate` BEFORE UPDATE ON `user` FOR EACH ROW begin
	if new.u_avatar='-1'
	then set new.u_avatar=old.u_avatar;
	end if;

	if new.u_nickname='-1'
	then set new.u_nickname=old.u_nickname;
	end if;

              if new.u_selfdes='-1'
	then set new.u_selfdes=old.u_selfdes;
	end if;

              if new.u_gender<=0
	then set new.u_gender=old.u_gender;
	end if;

              if new.u_birthday<=0
	then set new.u_birthday=old.u_birthday;
	end if;
 
end
;;
delimiter ;

-- ----------------------------
-- Triggers structure for table user_body
-- ----------------------------
DROP TRIGGER IF EXISTS `before_update`;
delimiter ;;
CREATE TRIGGER `before_update` BEFORE UPDATE ON `user_body` FOR EACH ROW begin
 
	if new.u_height<=0
	then set new.u_height=old.u_height;
	end if;

              if new.u_weight<=0
	then set new.u_weight=old.u_weight;
	end if;

              if new.u_vial_cap<=0
	then set new.u_vial_cap=old.u_vial_cap;
	end if;
 
end
;;
delimiter ;

SET FOREIGN_KEY_CHECKS = 1;

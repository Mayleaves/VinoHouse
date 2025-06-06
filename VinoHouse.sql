-- 创建数据库，使用 utf8mb4 字符集
DROP DATABASE IF EXISTS `VinoHouse_take_out`;
CREATE DATABASE `VinoHouse_take_out` CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE `VinoHouse_take_out`;

-- 地址簿表
DROP TABLE IF EXISTS `address_book`;
CREATE TABLE `address_book` (
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
    `user_id` bigint NOT NULL COMMENT '用户id',
    `consignee` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '收货人',
    `sex` varchar(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '性别',
    `phone` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '手机号',
    `province_code` varchar(12) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '省级区划编号',
    `province_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '省级名称',
    `city_code` varchar(12) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '市级区划编号',
    `city_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '市级名称',
    `district_code` varchar(12) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '区级区划编号',
    `district_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '区级名称',
    `detail` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '详细地址',
    `label` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '标签',
    `is_default` tinyint(1) NOT NULL DEFAULT '0' COMMENT '默认 0 否 1是',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='地址簿';

-- 分类表
DROP TABLE IF EXISTS `category`;
CREATE TABLE `category` (
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
    `type` int DEFAULT NULL COMMENT '类型 1 酒水分类 2 套餐分类',
    `name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '分类名称',
    `sort` int NOT NULL DEFAULT '0' COMMENT '顺序',
    `status` int DEFAULT NULL COMMENT '分类状态 0:禁用，1:启用',
    `create_time` datetime DEFAULT NULL COMMENT '创建时间',
    `update_time` datetime DEFAULT NULL COMMENT '更新时间',
    `create_user` bigint DEFAULT NULL COMMENT '创建人',
    `update_user` bigint DEFAULT NULL COMMENT '修改人',
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_category_name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='酒水及套餐分类';

-- 插入分类数据
INSERT INTO `category` VALUES
    (11,1,'精酿啤酒',10,1,'2023-11-05 14:23:17','2023-12-18 16:47:52',1,1),
    (12,1,'单一麦芽威士忌',9,1,'2023-11-10 09:18:45','2024-01-02 11:32:28',1,1),
    (16,1,'特调鸡尾酒',4,1,'2023-11-15 16:35:22','2023-12-25 20:17:39',1,1),
    (17,1,'进口红酒',5,1,'2023-11-20 13:48:56','2024-01-05 14:22:11',1,1),
    (18,1,'干邑白兰地',6,1,'2023-12-01 10:05:33','2024-01-10 17:33:47',1,1),
    (19,1,'精品伏特加',7,1,'2023-12-10 11:24:18','2024-01-12 09:48:02',1,1),
    (20,1,'无酒精特饮',8,1,'2023-12-15 15:12:59','2024-01-14 13:27:41',1,1),
    (21,1,'佐酒小食',11,1,'2023-12-20 18:32:14','2024-01-15 10:03:27',1,1),
    (13,2,'欢乐时光双人套餐',12,1,'2023-11-25 12:07:45','2024-01-08 19:04:38',1,1),
    (15,2,'VIP尊享品鉴套餐',13,1,'2023-12-05 17:49:21','2024-01-13 21:18:53',1,1);

-- 酒水表
DROP TABLE IF EXISTS `beverage`;
CREATE TABLE `beverage` (
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
    `name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '酒水名称',
    `category_id` bigint NOT NULL COMMENT '酒水分类id',
    `price` decimal(10,2) DEFAULT NULL COMMENT '酒水价格',
    `image` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '图片',
    `description` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '描述信息',
    `status` int DEFAULT '1' COMMENT '0 停售 1 起售',
    `create_time` datetime DEFAULT NULL COMMENT '创建时间',
    `update_time` datetime DEFAULT NULL COMMENT '更新时间',
    `create_user` bigint DEFAULT NULL COMMENT '创建人',
    `update_user` bigint DEFAULT NULL COMMENT '修改人',
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_beverage_name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=70 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='酒水';

-- 插入酒水数据
INSERT INTO `beverage` VALUES
    (46,'比利时白啤',11,48.00,'https://VinoHouse-itcast.oss-cn-beijing.aliyuncs.com/41bfcacf-7ad4-4927-8b26-df366553a94c.png','柑橘香气与微甜口感，酒精度4.8%',1,'2023-11-05 14:23:17','2023-12-18 16:47:52',1,1),
    (47,'IPA印度淡色艾尔',11,58.00,'https://VinoHouse-itcast.oss-cn-beijing.aliyuncs.com/4451d4be-89a2-4939-9c69-3a87151cb979.png','浓郁酒花香气，苦度适中，酒精度6.2%',1,'2023-11-10 09:18:45','2024-01-02 11:32:28',1,1),
    (48,'世涛黑啤',11,52.00,'https://VinoHouse-itcast.oss-cn-beijing.aliyuncs.com/bf8cbfc1-04d2-40e8-9826-061ee41ab87c.png','咖啡巧克力风味，口感醇厚，酒精度5.5%',1,'2023-11-15 16:35:22','2023-12-25 20:17:39',1,1),
    (49,'麦卡伦12年',12,128.00,'https://VinoHouse-itcast.oss-cn-beijing.aliyuncs.com/76752350-2121-44d2-b477-10791c23a8ec.png','雪莉桶陈酿，香草与干果风味',1,'2023-11-20 13:48:56','2024-01-05 14:22:11',1,1),
    (50,'山崎1923',12,198.00,'https://VinoHouse-itcast.oss-cn-beijing.aliyuncs.com/475cc599-8661-4899-8f9e-121dd8ef7d02.png','日本水楢桶陈酿，淡雅檀香风味',1,'2023-12-01 10:05:33','2024-01-10 17:33:47',1,1),
    (65,'莫吉托',16,68.00,'https://VinoHouse-itcast.oss-cn-beijing.aliyuncs.com/b544d3ba-a1ae-4d20-a860-81cb5dec9e03.png','白朗姆+青柠+薄荷+苏打水',1,'2023-12-05 17:49:21','2024-01-13 21:18:53',1,1),
    (66,'长岛冰茶',16,88.00,'https://VinoHouse-itcast.oss-cn-beijing.aliyuncs.com/a101a1e9-8f8b-47b2-afa4-1abd47ea0a87.png','五种基酒调配，后劲十足',1,'2023-12-10 11:24:18','2024-01-12 09:48:02',1,1),
    (67,'金汤力',16,58.00,'https://VinoHouse-itcast.oss-cn-beijing.aliyuncs.com/8cfcc576-4b66-4a09-ac68-ad5b273c2590.png','金酒+汤力水+青柠，清爽怡人',1,'2023-12-15 15:12:59','2024-01-14 13:27:41',1,1),
    (62,'波尔多干红',17,298.00,'https://VinoHouse-itcast.oss-cn-beijing.aliyuncs.com/7694a5d8-7938-4e9d-8b9e-2075983a2e38.png','法国2018年份，黑莓与橡木香气',1,'2023-12-20 18:32:14','2024-01-15 10:03:27',1,1),
    (63,'新西兰长相思',17,228.00,'https://VinoHouse-itcast.oss-cn-beijing.aliyuncs.com/f5ac8455-4793-450c-97ba-173795c34626.png','清新果香，酸度明亮',1,'2023-12-25 20:15:42','2024-01-14 22:37:19',1,1),
    (64,'纳帕谷赤霞珠',17,358.00,'https://VinoHouse-itcast.oss-cn-beijing.aliyuncs.com/7a55b845-1f2b-41fa-9486-76d187ee9ee1.png','美国加州产区，饱满酒体',1,'2023-12-28 16:47:35','2024-01-13 14:52:46',1,1),
    (58,'轩尼诗VSOP',18,188.00,'https://VinoHouse-itcast.oss-cn-beijing.aliyuncs.com/c18b5c67-3b71-466c-a75a-e63c6449f21c.png','法国干邑，香草和烤杏仁风味',1,'2024-01-02 12:33:24','2024-01-14 17:28:53',1,1),
    (59,'马爹利蓝带',18,388.00,'https://VinoHouse-itcast.oss-cn-beijing.aliyuncs.com/a80a4b8c-c93e-4f43-ac8a-856b0d5cc451.png','陈酿25年，圆润细腻',1,'2024-01-05 15:42:17','2024-01-15 12:37:45',1,1),
    (60,'人头马CLUB优质香槟区干邑',18,298.00,'https://VinoHouse-itcast.oss-cn-beijing.aliyuncs.com/6080b118-e30a-4577-aab4-45042e3f88be.png','精选大、小香槟区葡萄，口感醇厚，带有李子、肉桂与橡木香气',1,'2024-02-18 09:50:32','2024-02-25 14:12:47',1,1),
    (61,'拿破仑金尊XO干邑',18,888.00,'https://VinoHouse-itcast.oss-cn-beijing.aliyuncs.com/13da832f-ef2c-484d-8370-5934a1045a06.png','陈酿超25年，融合紫罗兰花香与蜜饯果香，单宁丝滑',1,'2024-04-03 13:25:46','2024-04-10 11:08:59',1,1),
    (54,'灰雁伏特加',19,128.00,'https://VinoHouse-itcast.oss-cn-beijing.aliyuncs.com/3613d38e-5614-41c2-90ed-ff175bf50716.png','法国产，顺滑纯净',1,'2024-01-08 18:23:56','2024-01-13 20:15:32',1,1),
    (55,'绝对伏特加',19,98.00,'https://VinoHouse-itcast.oss-cn-beijing.aliyuncs.com/4879ed66-3860-4b28-ba14-306ac025fdec.png','瑞典原装，多种口味可选',1,'2024-01-10 14:37:28','2024-01-14 16:42:19',1,1),
    (56,'雪树伏特加',19,328.00,'https://VinoHouse-itcast.oss-cn-beijing.aliyuncs.com/e9ec4ba4-4b22-4fc8-9be0-4946e6aeb937.png','波兰黑麦酿造，口感醇厚柔和，带有谷物清香',1,'2024-02-05 10:15:48','2024-02-10 14:28:30',1,1),
    (57,'皇太子伏特加',19,188.00,'https://VinoHouse-itcast.oss-cn-beijing.aliyuncs.com/22f59feb-0d44-430e-a6cd-6a49f27453ca.png','俄罗斯传统工艺，选用冬小麦，口感浓烈劲爽',1,'2024-03-12 16:30:15','2024-03-18 09:45:22',1,1),
    (51,'夏日微风',20,38.00,'https://VinoHouse-itcast.oss-cn-beijing.aliyuncs.com/4a9cefba-6a74-467e-9fde-6e687ea725d7.png','西柚+薄荷+苏打水',1,'2024-01-02 10:22:35','2024-01-15 11:45:18',1,1),
    (52,'莓果乐园',20,42.00,'https://VinoHouse-itcast.oss-cn-beijing.aliyuncs.com/5260ff39-986c-4a97-8850-2ec8c7583efc.png','混合莓果+柠檬汁+蜂蜜',1,'2024-01-05 14:38:47','2024-01-14 15:22:09',1,1),
    (53,'薄荷黄瓜饮',20,35.00,'https://VinoHouse-itcast.oss-cn-beijing.aliyuncs.com/a6953d5a-4c18-4b30-9319-4926ee77261f.png','新鲜黄瓜+薄荷叶+青柠+苏打水',1,'2024-01-09 15:33:18','2024-01-15 14:55:42',1,1),
    (68,'香辣鸡翅',21,48.00,'https://VinoHouse-itcast.oss-cn-beijing.aliyuncs.com/c09a0ee8-9d19-428d-81b9-746221824113.png','秘制辣酱腌制，外酥里嫩',1,'2024-01-08 16:53:24','2024-01-13 18:37:45',1,1),
    (69,'芝士拼盘',21,88.00,'https://VinoHouse-itcast.oss-cn-beijing.aliyuncs.com/16d0a3d6-2253-4cfc-9b49-bf7bd9eb2ad2.png','三种进口芝士配坚果和蜂蜜',1,'2024-01-10 12:27:16','2024-01-15 14:18:32',1,1);

-- 酒水口味关系表
DROP TABLE IF EXISTS `beverage_flavor`;
CREATE TABLE `beverage_flavor` (
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
    `beverage_id` bigint NOT NULL COMMENT '酒水',
    `name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '口味名称',
    `value` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '口味数据list',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=104 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='酒水口味关系表';

-- 插入酒水口味数据
INSERT INTO `beverage_flavor` VALUES
    (40,46,'温度','["常温","冰镇","加冰"]'),
    (41,46,'忌口','["不要橙片","不要柠檬角","坚果过敏","乳制品过敏"]'),
    (42,47,'温度','["常温","冰镇","加冰"]'),
    (43,47,'忌口','["不要酒花装饰","小麦过敏","酵母过敏"]'),
    (44,48,'温度','["常温","冰镇","加冰"]'),
    (45,48,'忌口','["不要巧克力碎","乳糖不耐","坚果过敏"]'),
    (46,49,'饮用方式','["纯饮","加冰","加苏打水"]'),
    (47,50,'冰球','["不加冰","标准冰球","大冰球"]'),
    (48,65,'甜度','["标准","少糖","半糖","全糖"]'),
    (49,65,'温度','["去冰","少冰","正常冰","加碎冰"]'),
    (50,65,'忌口','["不要薄荷叶","不要青柠","不要朗姆酒"]'),
    (51,66,'酒精度','["标准","轻度","加强"]'),
    (52,66,'温度','["去冰","少冰","正常冰"]'),
    (53,66,'忌口','["不要可乐","不要柠檬汁","不要糖浆"]'),
    (54,67,'酸度','["标准","微酸","加酸"]'),
    (55,67,'温度','["去冰","少冰","正常冰"]'),
    (56,67,'忌口','["不要青柠","不要金酒","奎宁过敏"]'),
    (57,62,'醒酒时间','["即饮","醒15分钟","醒30分钟"]'),
    (58,62,'饮用温度','["8-10℃","10-12℃","12-14℃"]'),
    (59,62,'杯型','["标准杯","大杯","醒酒器"]'),
    (60,63,'醒酒时间','["即饮","醒15分钟","醒30分钟"]'),
    (61,63,'饮用温度','["8-10℃","10-12℃","12-14℃"]'),
    (62,63,'杯型','["标准杯","大杯","醒酒器"]'),
    (63,64,'醒酒时间','["即饮","醒15分钟","醒30分钟"]'),
    (64,64,'饮用温度','["8-10℃","10-12℃","12-14℃"]'),
    (65,64,'杯型','["标准杯","大杯","醒酒器"]'),
    (66,58,'饮用方式','["纯饮","加冰","加干姜水","加可乐"]'),
    (67,59,'饮用方式','["纯饮","加冰球","加水","配雪茄"]'),
    (68,60,'饮用方式','["纯饮","加冰","调配咖啡鸡尾酒"]'),
    (69,61,'饮用方式','["纯饮","常温醒酒","加少量纯净水"]'),
    (70,54,'饮用方式','["纯饮","冷冻","调酒"]'),
    (71,55,'风味类型','["原味","柠檬味","草莓味","辣椒味","香草味"]'),
    (72,55,'饮用方式','["加冰","兑橙汁","兑红牛"]'),
    (73,56,'饮用方式','["纯饮","加冰","调制马天尼"]'),
    (74,57,'饮用方式','["加冰","配腌黄瓜","调制血腥玛丽"]'),
    (75,51,'甜味','["无糖","少糖","半糖","全糖"]'),
    (76,51,'温度','["去冰","少冰","正常冰","温热"]'),
    (77,51,'忌口','["不要西柚","不要薄荷","不要苏打水"]'),
    (78,52,'甜味','["无糖","少糖","半糖","全糖"]'),
    (79,52,'温度','["去冰","少冰","正常冰","热饮"]'),
    (80,52,'忌口','["不要草莓","不要蓝莓","不要柠檬汁"]'),
    (81,53,'甜味','["无糖","少糖","半糖","全糖"]'),
    (82,53,'温度','["去冰","少冰","正常冰","常温"]'),
    (83,53,'忌口','["不要黄瓜","不要薄荷","不要青柠"]'),
    (84,68,'辣度','["微辣","中辣","重辣","变态辣"]'),
    (85,68,'忌口','["不要辣","不要孜然","不要芝麻"]'),
    (86,69,'芝士类型','["切达芝士","布里芝士","蓝纹芝士","帕玛森芝士"]'),
    (87,69,'忌口','["不要坚果","不要蜂蜜","乳制品过敏"]');

-- 员工信息表
DROP TABLE IF EXISTS `employee`;
CREATE TABLE `employee` (
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
    `name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '姓名',
    `username` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '用户名',
    `password` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '密码',
    `phone` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '手机号',
    `sex` varchar(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '性别',
    `id_number` varchar(18) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '身份证号',
    `status` int NOT NULL DEFAULT '1' COMMENT '状态 0:禁用，1:启用',
    `create_time` datetime DEFAULT NULL COMMENT '创建时间',
    `update_time` datetime DEFAULT NULL COMMENT '更新时间',
    `create_user` bigint DEFAULT NULL COMMENT '创建人',
    `update_user` bigint DEFAULT NULL COMMENT '修改人',
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_username` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='员工信息';

INSERT INTO `employee` VALUES (1,'管理员','admin','123456','13898765432','1','220104200001011234',1,'2024-06-23 16:21:26','2024-06-27 15:26:40',10,1);

-- 订单明细表
DROP TABLE IF EXISTS `order_detail`;
CREATE TABLE `order_detail` (
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
    `name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '名字',
    `image` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '图片',
    `order_id` bigint NOT NULL COMMENT '订单id',
    `beverage_id` bigint DEFAULT NULL COMMENT '酒水id',
    `setmeal_id` bigint DEFAULT NULL COMMENT '套餐id',
    `beverage_flavor` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '口味',
    `number` int NOT NULL DEFAULT '1' COMMENT '数量',
    `amount` decimal(10,2) NOT NULL COMMENT '金额',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='订单明细表';

-- 订单表
DROP TABLE IF EXISTS `orders`;
CREATE TABLE `orders` (
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
    `number` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '订单号',
    `status` int NOT NULL DEFAULT '1' COMMENT '订单状态 1待付款 2待接单 3已接单 4派送中 5已完成 6已取消 7退款',
    `user_id` bigint NOT NULL COMMENT '下单用户',
    `address_book_id` bigint NOT NULL COMMENT '地址id',
    `order_time` datetime NOT NULL COMMENT '下单时间',
    `checkout_time` datetime DEFAULT NULL COMMENT '结账时间',
    `pay_method` int NOT NULL DEFAULT '1' COMMENT '支付方式 1微信,2支付宝',
    `pay_status` tinyint NOT NULL DEFAULT '0' COMMENT '支付状态 0未支付 1已支付 2退款',
    `amount` decimal(10,2) NOT NULL COMMENT '实收金额',
    `remark` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '备注',
    `phone` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '手机号',
    `address` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '地址',
    `user_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '用户名称',
    `consignee` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '收货人',
    `cancel_reason` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '订单取消原因',
    `rejection_reason` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '订单拒绝原因',
    `cancel_time` datetime DEFAULT NULL COMMENT '订单取消时间',
    `estimated_delivery_time` datetime DEFAULT NULL COMMENT '预计送达时间',
    `delivery_status` tinyint(1) NOT NULL DEFAULT '1' COMMENT '配送状态 1立即送出 0选择具体时间',
    `delivery_time` datetime DEFAULT NULL COMMENT '送达时间',
    `pack_amount` int DEFAULT NULL COMMENT '打包费',
    `tableware_number` int DEFAULT NULL COMMENT '餐具数量',
    `tableware_status` tinyint(1) NOT NULL DEFAULT '1' COMMENT '餐具数量状态 1按餐量提供 0选择具体数量',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='订单表';

-- 套餐表
DROP TABLE IF EXISTS `setmeal`;
CREATE TABLE `setmeal` (
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
    `category_id` bigint NOT NULL COMMENT '酒水分类id',
    `name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '套餐名称',
    `price` decimal(10,2) NOT NULL COMMENT '套餐价格',
    `status` int DEFAULT '1' COMMENT '售卖状态 0:停售 1:起售',
    `description` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '描述信息',
    `image` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '图片',
    `create_time` datetime DEFAULT NULL COMMENT '创建时间',
    `update_time` datetime DEFAULT NULL COMMENT '更新时间',
    `create_user` bigint DEFAULT NULL COMMENT '创建人',
    `update_user` bigint DEFAULT NULL COMMENT '修改人',
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_setmeal_name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=32 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='套餐';

-- 套餐酒水关系表
DROP TABLE IF EXISTS `setmeal_beverage`;
CREATE TABLE `setmeal_beverage` (
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
    `setmeal_id` bigint DEFAULT NULL COMMENT '套餐id',
    `beverage_id` bigint DEFAULT NULL COMMENT '酒水id',
    `name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '酒水名称（冗余字段）',
    `price` decimal(10,2) DEFAULT NULL COMMENT '酒水单价（冗余字段）',
    `copies` int DEFAULT NULL COMMENT '酒水份数',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=47 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='套餐酒水关系';

-- 购物车表
DROP TABLE IF EXISTS `shopping_cart`;
CREATE TABLE `shopping_cart` (
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
    `name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '商品名称',
    `image` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '图片',
    `user_id` bigint NOT NULL COMMENT '主键',
    `beverage_id` bigint DEFAULT NULL COMMENT '酒水id',
    `setmeal_id` bigint DEFAULT NULL COMMENT '套餐id',
    `beverage_flavor` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '口味',
    `number` int NOT NULL DEFAULT '1' COMMENT '数量',
    `amount` decimal(10,2) NOT NULL COMMENT '金额',
    `create_time` datetime DEFAULT NULL COMMENT '创建时间',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='购物车';

-- 用户信息表
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
    `openid` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '微信用户唯一标识',
    `name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '姓名',
    `phone` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '手机号',
    `sex` varchar(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '性别',
    `id_number` varchar(18) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '身份证号',
    `avatar` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '头像',
    `create_time` datetime DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户信息';
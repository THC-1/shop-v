USE shop_v3;

-- 商品数据 (25个商品, ID从1到25)
INSERT INTO `products` (`name`, `description`, `images`, `attributes`, `original_price`, `price`, `stock`, `created_at`) VALUES
-- 1. iPhone 15 Pro Max
('iPhone 15 Pro Max', '苹果旗舰手机，A17 Pro芯片，钛金属设计，4800万像素主摄，支持灵动岛，续航能力超强。', '["https://picsum.photos/seed/iphone15/800/800"]', '{"颜色": ["原色钛金属", "蓝色钛金属", "白色钛金属", "黑色钛金属"], "存储": ["256GB", "512GB", "1TB"]}', 9999.00, 8999.00, 100, NOW()),
-- 2. 小米14 Ultra
('小米14 Ultra', '徕卡光学Summilux镜头，骁龙8 Gen3处理器，5300mAh大电池，支持双向卫星通信。', '["https://picsum.photos/seed/xiaomi14/800/800"]', '{"颜色": ["黑色", "白色", "钛金属色"], "存储": ["256GB", "512GB", "1TB"]}', 6499.00, 5999.00, 200, NOW()),
-- 3. 华为Mate 60 Pro
('华为Mate 60 Pro', '麒麟9000S芯片，XMAGE影像，超可靠玄武架构，支持天通卫星通话。', '["https://picsum.photos/seed/mate60/800/800"]', '{"颜色": ["雅丹黑", "雅鲁藏布白", "南糯紫", "白沙银"], "存储": ["256GB", "512GB", "1TB"]}', 6999.00, 6499.00, 150, NOW()),
-- 4. 三星Galaxy S24 Ultra
('三星Galaxy S24 Ultra', '骁龙8 Gen3处理器，2亿像素主摄，内置S Pen，钛金属边框。', '["https://picsum.photos/seed/s24ultra/800/800"]', '{"颜色": ["钛灰", "钛黑", "钛暮紫", "钛羽黄"], "存储": ["256GB", "512GB", "1TB"]}', 9699.00, 8799.00, 120, NOW()),
-- 5. MacBook Pro 16英寸 M3 Max
('MacBook Pro 16英寸 M3 Max', 'M3 Max芯片，36GB统一内存，1TB固态硬盘，Liquid视网膜XDR显示屏。', '["https://picsum.photos/seed/macbook16/800/800"]', '{"颜色": ["银色", "深空黑色"], "存储": ["1TB", "2TB", "4TB"]}', 27999.00, 24999.00, 50, NOW()),
-- 6. ThinkPad X1 Carbon AI 2024
('ThinkPad X1 Carbon AI 2024', '英特尔酷睿Ultra7处理器，32GB内存，1TB PCIe固态，轻至1.11kg。', '["https://picsum.photos/seed/thinkpadx1/800/800"]', '{"颜色": ["黑色"], "内存": ["32GB"], "存储": ["512GB", "1TB", "2TB"]}', 14999.00, 12999.00, 80, NOW()),
-- 7. 华为MateBook X Pro
('华为MateBook X Pro', '英特尔酷睿Ultra9处理器，32GB内存，柔性OLED原色屏，支持多屏协同。', '["https://picsum.photos/seed/matebook/800/800"]', '{"颜色": ["晴蓝", "砚黑", "宣白"], "内存": ["32GB"], "存储": ["1TB", "2TB"]}', 13999.00, 11999.00, 60, NOW()),
-- 8. ROG枪神8 Plus
('ROG枪神8 Plus', '14代英特尔酷睿i9处理器，RTX 4070显卡，液金导热，18英寸高刷电竞屏。', '["https://picsum.photos/seed/rog/800/800"]', '{"颜色": ["日蚀灰", "魔酷绿"], "显卡": ["RTX 4060", "RTX 4070", "RTX 4080"], "存储": ["1TB", "2TB"]}', 17999.00, 15999.00, 40, NOW()),
-- 9. iPad Pro 12.9英寸 M4
('iPad Pro 12.9英寸 M4', 'Apple M4芯片，Liquid视网膜XDR显示屏，支持Apple Pencil Pro和妙控键盘。', '["https://picsum.photos/seed/ipadpro/800/800"]', '{"颜色": ["银色", "深空灰色"], "存储": ["256GB", "512GB", "1TB", "2TB"], "网络": ["Wi-Fi", "Wi-Fi+蜂窝网络"]}', 11499.00, 9999.00, 90, NOW()),
-- 10. 华为MatePad Pro 13.2英寸
('华为MatePad Pro 13.2英寸', '麒麟9000S处理器，13.2英寸柔性OLED屏，HarmonyOS 4操作系统。', '["https://picsum.photos/seed/matepad/800/800"]', '{"颜色": ["曜金色", "晶钻白", "罗兰紫", "星云灰"], "存储": ["256GB", "512GB", "1TB"]}', 5999.00, 5199.00, 100, NOW()),
-- 11. Apple Watch Ultra 2
('Apple Watch Ultra 2', '钛金属表壳，蓝宝石玻璃，100米防水，双频GPS，36小时续航。', '["https://picsum.photos/seed/watchultra/800/800"]', '{"颜色": ["原色钛金属"], "表带": ["Alpine回环式表带", "高山回环式表带", "野径回环式表带"]}', 6499.00, 5899.00, 70, NOW()),
-- 12. 华为WATCH GT 4
('华为WATCH GT 4', '46mm圆形表盘，14天超长续航，100+运动模式，麒麟A2芯片。', '["https://picsum.photos/seed/watchgt/800/800"]', '{"颜色": ["曜石化", "云杉绿", "山茶棕", "皓月银"], "表盘尺寸": ["46mm", "41mm"]}', 1788.00, 1588.00, 200, NOW()),
-- 13. 小米手表S3
('小米手表S3', '1.43英寸AMOLED大屏，12天续航，150+运动模式，支持eSIM独立通话。', '["https://picsum.photos/seed/xiaomiwatch/800/800"]', '{"颜色": ["黑色", "银色", "原色"], "表圈": ["不锈钢", "陶瓷"]}', 999.00, 799.00, 180, NOW()),
-- 14. AirPods Pro (第二代)
('AirPods Pro (第二代)', '自适应音频，个性化空间音频，MagSafe充电盒，6小时聆听时间。', '["https://picsum.photos/seed/airpods/800/800"]', '{"充电盒": ["MagSafe充电盒", "USB-C充电盒"], "配件": ["含MagSafe充电盒"]}', 1899.00, 1699.00, 300, NOW()),
-- 15. 华为FreeBuds Pro 3
('华为FreeBuds Pro 3', '麒麟A2芯片，星闪核心技术，1.5Mbps无损音质，智能动态降噪3.0。', '["https://picsum.photos/seed/freebuds/800/800"]', '{"颜色": ["陶瓷白", "星河蓝", "冰霜银", "砚黑"], "款式": ["标准版", "眼镜蛇特别版"]}', 1499.00, 1299.00, 250, NOW()),
-- 16. 索尼WH-1000XM5
('索尼WH-1000XM5', '30小时续航，8麦克风AI降噪，LDAC蓝牙传输，360临场音效。', '["https://picsum.photos/seed/sonyxm5/800/800"]', '{"颜色": ["黑色", "铂金银", "深夜蓝"]}', 2699.00, 2299.00, 120, NOW()),
-- 17. PlayStation 5 光驱版
('PlayStation 5 光驱版', 'AMD Ryzen处理器，RDNA2架构显卡，825GB SSD，支持4K蓝光光驱。', '["https://picsum.photos/seed/ps5/800/800"]', '{"版本": ["光驱版", "数字版"], "套装": ["单主机", "双手柄套装"]}', 3999.00, 3599.00, 60, NOW()),
-- 18. Nintendo Switch OLED
('Nintendo Switch OLED', '7英寸OLED屏幕，95GB内置存储，桌面模式/手持模式/TV模式。', '["https://picsum.photos/seed/switch/800/800"]', '{"颜色": ["白色", "电光红-蓝"], "套装": ["标准版", "精灵宝可梦朱紫限定版"]}', 2599.00, 2349.00, 100, NOW()),
-- 19. 小米扫地机器人3C+
('小米扫地机器人3C+', '4000Pa大吸力，激光导航，智能分区管理，支持小爱同学控制。', '["https://picsum.photos/seed/robot3c/800/800"]', '{"颜色": ["白色"]}', 1499.00, 999.00, 150, NOW()),
-- 20. 石头扫地机器人G20S
('石头扫地机器人G20S', '6000Pa吸力，声波震动擦地，AI避障3.0，自动集尘+自动洗拖布。', '["https://picsum.photos/seed/roborock/800/800"]', '{"颜色": ["黑色", "白色"]}', 5999.00, 4999.00, 80, NOW()),
-- 21. 小米智能门锁M20 Pro
('小米智能门锁M20 Pro', '3D结构光人脸识别，指静脉识别，180°超广角摄像头，逗留侦测。', '["https://picsum.photos/seed/smartlock/800/800"]', '{"颜色": ["黑色"]}', 2999.00, 2299.00, 90, NOW()),
-- 22. 三星990 PRO 2TB SSD
('三星990 PRO 2TB SSD', 'PCIe 4.0 NVMe协议，7450MB/s读取速度，五年质保。', '["https://picsum.photos/seed/ssd/800/800"]', '{"容量": ["1TB", "2TB", "4TB"]}', 1599.00, 1399.00, 200, NOW()),
-- 23. 西部数据My Passport 5TB
('西部数据My Passport 5TB', 'USB 3.2 Gen 1接口，256位AES硬件加密，兼容Mac/Windows。', '["https://picsum.photos/seed/wdpassport/800/800"]', '{"颜色": ["黑色", "白色", "蓝色"], "容量": ["2TB", "4TB", "5TB"]}', 899.00, 799.00, 180, NOW()),
-- 24. Anker 100W氮化镓充电器
('Anker 100W氮化镓充电器', 'GaN技术，3个充电口，单口最大100W，支持PD3.0/PPS。', '["https://picsum.photos/seed/anker100w/800/800"]', '{"颜色": ["黑色", "白色"]}', 299.00, 199.00, 500, NOW()),
-- 25. 小米移动电源3 20000mAh
('小米移动电源3 20000mAh', '20000mAh大容量，50W MAX输出，支持双向快充，三口同时输出。', '["https://picsum.photos/seed/powerbank/800/800"]', '{"颜色": ["银色"]}', 199.00, 149.00, 400, NOW());

-- SKU数据 (根据商品ID关联, product_id 1-25)
INSERT INTO `skus` (`product_id`, `name`, `spec_values`, `price`, `stock`, `image`, `created_at`) VALUES
-- iPhone 15 Pro Max (product_id=1) - 4颜色 x 3存储 = 12个SKU
(1, '原色钛金属 256GB', '{"颜色": "原色钛金属", "存储": "256GB"}', 8999.00, 30, 'https://picsum.photos/seed/iphone15-1/400/400', NOW()),
(1, '原色钛金属 512GB', '{"颜色": "原色钛金属", "存储": "512GB"}', 9999.00, 25, 'https://picsum.photos/seed/iphone15-2/400/400', NOW()),
(1, '原色钛金属 1TB', '{"颜色": "原色钛金属", "存储": "1TB"}', 11999.00, 15, 'https://picsum.photos/seed/iphone15-3/400/400', NOW()),
(1, '蓝色钛金属 256GB', '{"颜色": "蓝色钛金属", "存储": "256GB"}', 8999.00, 30, 'https://picsum.photos/seed/iphone15-4/400/400', NOW()),
(1, '蓝色钛金属 512GB', '{"颜色": "蓝色钛金属", "存储": "512GB"}', 9999.00, 20, 'https://picsum.photos/seed/iphone15-5/400/400', NOW()),
(1, '蓝色钛金属 1TB', '{"颜色": "蓝色钛金属", "存储": "1TB"}', 11999.00, 10, 'https://picsum.photos/seed/iphone15-6/400/400', NOW()),
(1, '白色钛金属 256GB', '{"颜色": "白色钛金属", "存储": "256GB"}', 8999.00, 30, 'https://picsum.photos/seed/iphone15-7/400/400', NOW()),
(1, '白色钛金属 512GB', '{"颜色": "白色钛金属", "存储": "512GB"}', 9999.00, 20, 'https://picsum.photos/seed/iphone15-8/400/400', NOW()),
(1, '白色钛金属 1TB', '{"颜色": "白色钛金属", "存储": "1TB"}', 11999.00, 10, 'https://picsum.photos/seed/iphone15-9/400/400', NOW()),
(1, '黑色钛金属 256GB', '{"颜色": "黑色钛金属", "存储": "256GB"}', 8999.00, 30, 'https://picsum.photos/seed/iphone15-10/400/400', NOW()),
(1, '黑色钛金属 512GB', '{"颜色": "黑色钛金属", "存储": "512GB"}', 9999.00, 20, 'https://picsum.photos/seed/iphone15-11/400/400', NOW()),
(1, '黑色钛金属 1TB', '{"颜色": "黑色钛金属", "存储": "1TB"}', 11999.00, 10, 'https://picsum.photos/seed/iphone15-12/400/400', NOW()),

-- 小米14 Ultra (product_id=2) - 3颜色 x 3存储 = 9个SKU
(2, '黑色 256GB', '{"颜色": "黑色", "存储": "256GB"}', 5999.00, 40, 'https://picsum.photos/seed/xiaomi14-1/400/400', NOW()),
(2, '黑色 512GB', '{"颜色": "黑色", "存储": "512GB"}', 6499.00, 35, 'https://picsum.photos/seed/xiaomi14-2/400/400', NOW()),
(2, '黑色 1TB', '{"颜色": "黑色", "存储": "1TB"}', 7299.00, 25, 'https://picsum.photos/seed/xiaomi14-3/400/400', NOW()),
(2, '白色 256GB', '{"颜色": "白色", "存储": "256GB"}', 5999.00, 40, 'https://picsum.photos/seed/xiaomi14-4/400/400', NOW()),
(2, '白色 512GB', '{"颜色": "白色", "存储": "512GB"}', 6499.00, 35, 'https://picsum.photos/seed/xiaomi14-5/400/400', NOW()),
(2, '白色 1TB', '{"颜色": "白色", "存储": "1TB"}', 7299.00, 25, 'https://picsum.photos/seed/xiaomi14-6/400/400', NOW()),
(2, '钛金属色 256GB', '{"颜色": "钛金属色", "存储": "256GB"}', 5999.00, 40, 'https://picsum.photos/seed/xiaomi14-7/400/400', NOW()),
(2, '钛金属色 512GB', '{"颜色": "钛金属色", "存储": "512GB"}', 6499.00, 35, 'https://picsum.photos/seed/xiaomi14-8/400/400', NOW()),
(2, '钛金属色 1TB', '{"颜色": "钛金属色", "存储": "1TB"}', 7299.00, 25, 'https://picsum.photos/seed/xiaomi14-9/400/400', NOW()),

-- 华为Mate 60 Pro (product_id=3) - 4颜色 x 3存储 = 12个SKU
(3, '雅丹黑 256GB', '{"颜色": "雅丹黑", "存储": "256GB"}', 6499.00, 35, 'https://picsum.photos/seed/mate60-1/400/400', NOW()),
(3, '雅丹黑 512GB', '{"颜色": "雅丹黑", "存储": "512GB"}', 6999.00, 30, 'https://picsum.photos/seed/mate60-2/400/400', NOW()),
(3, '雅丹黑 1TB', '{"颜色": "雅丹黑", "存储": "1TB"}', 7999.00, 20, 'https://picsum.photos/seed/mate60-3/400/400', NOW()),
(3, '雅鲁藏布白 256GB', '{"颜色": "雅鲁藏布白", "存储": "256GB"}', 6499.00, 35, 'https://picsum.photos/seed/mate60-4/400/400', NOW()),
(3, '雅鲁藏布白 512GB', '{"颜色": "雅鲁藏布白", "存储": "512GB"}', 6999.00, 30, 'https://picsum.photos/seed/mate60-5/400/400', NOW()),
(3, '雅鲁藏布白 1TB', '{"颜色": "雅鲁藏布白", "存储": "1TB"}', 7999.00, 20, 'https://picsum.photos/seed/mate60-6/400/400', NOW()),
(3, '南糯紫 256GB', '{"颜色": "南糯紫", "存储": "256GB"}', 6499.00, 35, 'https://picsum.photos/seed/mate60-7/400/400', NOW()),
(3, '南糯紫 512GB', '{"颜色": "南糯紫", "存储": "512GB"}', 6999.00, 30, 'https://picsum.photos/seed/mate60-8/400/400', NOW()),
(3, '南糯紫 1TB', '{"颜色": "南糯紫", "存储": "1TB"}', 7999.00, 20, 'https://picsum.photos/seed/mate60-9/400/400', NOW()),
(3, '白沙银 256GB', '{"颜色": "白沙银", "存储": "256GB"}', 6499.00, 35, 'https://picsum.photos/seed/mate60-10/400/400', NOW()),
(3, '白沙银 512GB', '{"颜色": "白沙银", "存储": "512GB"}', 6999.00, 30, 'https://picsum.photos/seed/mate60-11/400/400', NOW()),
(3, '白沙银 1TB', '{"颜色": "白沙银", "存储": "1TB"}', 7999.00, 20, 'https://picsum.photos/seed/mate60-12/400/400', NOW()),

-- 三星Galaxy S24 Ultra (product_id=4) - 4颜色 x 3存储 = 12个SKU
(4, '钛灰 256GB', '{"颜色": "钛灰", "存储": "256GB"}', 8799.00, 30, 'https://picsum.photos/seed/s24ultra-1/400/400', NOW()),
(4, '钛灰 512GB', '{"颜色": "钛灰", "存储": "512GB"}', 9699.00, 25, 'https://picsum.photos/seed/s24ultra-2/400/400', NOW()),
(4, '钛灰 1TB', '{"颜色": "钛灰", "存储": "1TB"}', 11699.00, 15, 'https://picsum.photos/seed/s24ultra-3/400/400', NOW()),
(4, '钛黑 256GB', '{"颜色": "钛黑", "存储": "256GB"}', 8799.00, 30, 'https://picsum.photos/seed/s24ultra-4/400/400', NOW()),
(4, '钛黑 512GB', '{"颜色": "钛黑", "存储": "512GB"}', 9699.00, 25, 'https://picsum.photos/seed/s24ultra-5/400/400', NOW()),
(4, '钛黑 1TB', '{"颜色": "钛黑", "存储": "1TB"}', 11699.00, 15, 'https://picsum.photos/seed/s24ultra-6/400/400', NOW()),
(4, '钛暮紫 256GB', '{"颜色": "钛暮紫", "存储": "256GB"}', 8799.00, 30, 'https://picsum.photos/seed/s24ultra-7/400/400', NOW()),
(4, '钛暮紫 512GB', '{"颜色": "钛暮紫", "存储": "512GB"}', 9699.00, 25, 'https://picsum.photos/seed/s24ultra-8/400/400', NOW()),
(4, '钛暮紫 1TB', '{"颜色": "钛暮紫", "存储": "1TB"}', 11699.00, 15, 'https://picsum.photos/seed/s24ultra-9/400/400', NOW()),
(4, '钛羽黄 256GB', '{"颜色": "钛羽黄", "存储": "256GB"}', 8799.00, 30, 'https://picsum.photos/seed/s24ultra-10/400/400', NOW()),
(4, '钛羽黄 512GB', '{"颜色": "钛羽黄", "存储": "512GB"}', 9699.00, 25, 'https://picsum.photos/seed/s24ultra-11/400/400', NOW()),
(4, '钛羽黄 1TB', '{"颜色": "钛羽黄", "存储": "1TB"}', 11699.00, 15, 'https://picsum.photos/seed/s24ultra-12/400/400', NOW()),

-- MacBook Pro 16英寸 M3 Max (product_id=5) - 2颜色 x 3存储 = 6个SKU
(5, '银色 1TB', '{"颜色": "银色", "存储": "1TB"}', 24999.00, 20, 'https://picsum.photos/seed/macbook16-1/400/400', NOW()),
(5, '银色 2TB', '{"颜色": "银色", "存储": "2TB"}', 27999.00, 15, 'https://picsum.photos/seed/macbook16-2/400/400', NOW()),
(5, '银色 4TB', '{"颜色": "银色", "存储": "4TB"}', 34999.00, 5, 'https://picsum.photos/seed/macbook16-3/400/400', NOW()),
(5, '深空黑色 1TB', '{"颜色": "深空黑色", "存储": "1TB"}', 24999.00, 20, 'https://picsum.photos/seed/macbook16-4/400/400', NOW()),
(5, '深空黑色 2TB', '{"颜色": "深空黑色", "存储": "2TB"}', 27999.00, 15, 'https://picsum.photos/seed/macbook16-5/400/400', NOW()),
(5, '深空黑色 4TB', '{"颜色": "深空黑色", "存储": "4TB"}', 34999.00, 5, 'https://picsum.photos/seed/macbook16-6/400/400', NOW()),

-- ThinkPad X1 Carbon AI 2024 (product_id=6) - 1颜色 x 3存储 = 3个SKU
(6, '黑色 512GB', '{"颜色": "黑色", "存储": "512GB"}', 12999.00, 30, 'https://picsum.photos/seed/thinkpadx1-1/400/400', NOW()),
(6, '黑色 1TB', '{"颜色": "黑色", "存储": "1TB"}', 13999.00, 35, 'https://picsum.photos/seed/thinkpadx1-2/400/400', NOW()),
(6, '黑色 2TB', '{"颜色": "黑色", "存储": "2TB"}', 15999.00, 15, 'https://picsum.photos/seed/thinkpadx1-3/400/400', NOW()),

-- 华为MateBook X Pro (product_id=7) - 3颜色 x 2存储 = 6个SKU
(7, '晴蓝 1TB', '{"颜色": "晴蓝", "存储": "1TB"}', 11999.00, 20, 'https://picsum.photos/seed/matebook-1/400/400', NOW()),
(7, '晴蓝 2TB', '{"颜色": "晴蓝", "存储": "2TB"}', 13999.00, 10, 'https://picsum.photos/seed/matebook-2/400/400', NOW()),
(7, '砚黑 1TB', '{"颜色": "砚黑", "存储": "1TB"}', 11999.00, 20, 'https://picsum.photos/seed/matebook-3/400/400', NOW()),
(7, '砚黑 2TB', '{"颜色": "砚黑", "存储": "2TB"}', 13999.00, 10, 'https://picsum.photos/seed/matebook-4/400/400', NOW()),
(7, '宣白 1TB', '{"颜色": "宣白", "存储": "1TB"}', 11999.00, 20, 'https://picsum.photos/seed/matebook-5/400/400', NOW()),
(7, '宣白 2TB', '{"颜色": "宣白", "存储": "2TB"}', 13999.00, 10, 'https://picsum.photos/seed/matebook-6/400/400', NOW()),

-- ROG枪神8 Plus (product_id=8) - 2颜色 x 2显卡 x 2存储 = 8个SKU
(8, '日蚀灰 RTX4060 1TB', '{"颜色": "日蚀灰", "显卡": "RTX 4060", "存储": "1TB"}', 14999.00, 15, 'https://picsum.photos/seed/rog-1/400/400', NOW()),
(8, '日蚀灰 RTX4060 2TB', '{"颜色": "日蚀灰", "显卡": "RTX 4060", "存储": "2TB"}', 16999.00, 10, 'https://picsum.photos/seed/rog-2/400/400', NOW()),
(8, '日蚀灰 RTX4070 1TB', '{"颜色": "日蚀灰", "显卡": "RTX 4070", "存储": "1TB"}', 15999.00, 20, 'https://picsum.photos/seed/rog-3/400/400', NOW()),
(8, '日蚀灰 RTX4070 2TB', '{"颜色": "日蚀灰", "显卡": "RTX 4070", "存储": "2TB"}', 17999.00, 15, 'https://picsum.photos/seed/rog-4/400/400', NOW()),
(8, '魔酷绿 RTX4060 1TB', '{"颜色": "魔酷绿", "显卡": "RTX 4060", "存储": "1TB"}', 14999.00, 15, 'https://picsum.photos/seed/rog-5/400/400', NOW()),
(8, '魔酷绿 RTX4060 2TB', '{"颜色": "魔酷绿", "显卡": "RTX 4060", "存储": "2TB"}', 16999.00, 10, 'https://picsum.photos/seed/rog-6/400/400', NOW()),
(8, '魔酷绿 RTX4070 1TB', '{"颜色": "魔酷绿", "显卡": "RTX 4070", "存储": "1TB"}', 15999.00, 20, 'https://picsum.photos/seed/rog-7/400/400', NOW()),
(8, '魔酷绿 RTX4070 2TB', '{"颜色": "魔酷绿", "显卡": "RTX 4070", "存储": "2TB"}', 17999.00, 15, 'https://picsum.photos/seed/rog-8/400/400', NOW()),

-- iPad Pro (product_id=9) - 2颜色 x 4存储 x 2网络 = 16个SKU (简化取6个)
(9, '银色 256GB Wi-Fi', '{"颜色": "银色", "存储": "256GB", "网络": "Wi-Fi"}', 9999.00, 25, 'https://picsum.photos/seed/ipadpro-1/400/400', NOW()),
(9, '银色 512GB Wi-Fi', '{"颜色": "银色", "存储": "512GB", "网络": "Wi-Fi"}', 10999.00, 20, 'https://picsum.photos/seed/ipadpro-2/400/400', NOW()),
(9, '银色 1TB Wi-Fi', '{"颜色": "银色", "存储": "1TB", "网络": "Wi-Fi"}', 12499.00, 15, 'https://picsum.photos/seed/ipadpro-3/400/400', NOW()),
(9, '银色 256GB Wi-Fi+蜂窝', '{"颜色": "银色", "存储": "256GB", "网络": "Wi-Fi+蜂窝网络"}', 11999.00, 15, 'https://picsum.photos/seed/ipadpro-4/400/400', NOW()),
(9, '深空灰 256GB Wi-Fi', '{"颜色": "深空灰色", "存储": "256GB", "网络": "Wi-Fi"}', 9999.00, 25, 'https://picsum.photos/seed/ipadpro-5/400/400', NOW()),
(9, '深空灰 512GB Wi-Fi', '{"颜色": "深空灰色", "存储": "512GB", "网络": "Wi-Fi"}', 10999.00, 20, 'https://picsum.photos/seed/ipadpro-6/400/400', NOW()),

-- 华为MatePad Pro (product_id=10) - 4颜色 x 3存储 = 12个SKU (简化取4个)
(10, '曜金色 256GB', '{"颜色": "曜金色", "存储": "256GB"}', 5199.00, 30, 'https://picsum.photos/seed/matepad-1/400/400', NOW()),
(10, '晶钻白 256GB', '{"颜色": "晶钻白", "存储": "256GB"}', 5199.00, 30, 'https://picsum.photos/seed/matepad-2/400/400', NOW()),
(10, '罗兰紫 256GB', '{"颜色": "罗兰紫", "存储": "256GB"}', 5199.00, 30, 'https://picsum.photos/seed/matepad-3/400/400', NOW()),
(10, '星云灰 256GB', '{"颜色": "星云灰", "存储": "256GB"}', 5199.00, 30, 'https://picsum.photos/seed/matepad-4/400/400', NOW()),

-- Apple Watch Ultra 2 (product_id=11) - 1颜色 x 3表带 = 3个SKU
(11, '原色钛金属 Alpine回环式', '{"颜色": "原色钛金属", "表带": "Alpine回环式表带"}', 5899.00, 30, 'https://picsum.photos/seed/watchultra-1/400/400', NOW()),
(11, '原色钛金属 高山回环式', '{"颜色": "原色钛金属", "表带": "高山回环式表带"}', 5899.00, 25, 'https://picsum.photos/seed/watchultra-2/400/400', NOW()),
(11, '原色钛金属 野径回环式', '{"颜色": "原色钛金属", "表带": "野径回环式表带"}', 5899.00, 25, 'https://picsum.photos/seed/watchultra-3/400/400', NOW()),

-- 华为WATCH GT 4 (product_id=12) - 4颜色 x 2尺寸 = 8个SKU (简化取4个)
(12, '曜石化 46mm', '{"颜色": "曜石化", "表盘尺寸": "46mm"}', 1588.00, 50, 'https://picsum.photos/seed/watchgt-1/400/400', NOW()),
(12, '云杉绿 46mm', '{"颜色": "云杉绿", "表盘尺寸": "46mm"}', 1588.00, 50, 'https://picsum.photos/seed/watchgt-2/400/400', NOW()),
(12, '山茶棕 46mm', '{"颜色": "山茶棕", "表盘尺寸": "46mm"}', 1588.00, 50, 'https://picsum.photos/seed/watchgt-3/400/400', NOW()),
(12, '皓月银 41mm', '{"颜色": "皓月银", "表盘尺寸": "41mm"}', 1388.00, 50, 'https://picsum.photos/seed/watchgt-4/400/400', NOW()),

-- 小米手表S3 (product_id=13) - 3颜色 x 2表圈 = 6个SKU (简化取3个)
(13, '黑色 不锈钢', '{"颜色": "黑色", "表圈": "不锈钢"}', 799.00, 60, 'https://picsum.photos/seed/xiaomiwatch-1/400/400', NOW()),
(13, '银色 不锈钢', '{"颜色": "银色", "表圈": "不锈钢"}', 799.00, 60, 'https://picsum.photos/seed/xiaomiwatch-2/400/400', NOW()),
(13, '原色 不锈钢', '{"颜色": "原色", "表圈": "不锈钢"}', 799.00, 60, 'https://picsum.photos/seed/xiaomiwatch-3/400/400', NOW()),

-- AirPods Pro (product_id=14) - 2充电盒 = 2个SKU
(14, 'MagSafe充电盒', '{"充电盒": "MagSafe充电盒", "配件": "含MagSafe充电盒"}', 1699.00, 150, 'https://picsum.photos/seed/airpods-1/400/400', NOW()),
(14, 'USB-C充电盒', '{"充电盒": "USB-C充电盒", "配件": "含USB-C充电盒"}', 1699.00, 150, 'https://picsum.photos/seed/airpods-2/400/400', NOW()),

-- 华为FreeBuds Pro 3 (product_id=15) - 4颜色 x 2款式 = 8个SKU (简化取4个)
(15, '陶瓷白 标准版', '{"颜色": "陶瓷白", "款式": "标准版"}', 1299.00, 70, 'https://picsum.photos/seed/freebuds-1/400/400', NOW()),
(15, '星河蓝 标准版', '{"颜色": "星河蓝", "款式": "标准版"}', 1299.00, 70, 'https://picsum.photos/seed/freebuds-2/400/400', NOW()),
(15, '冰霜银 标准版', '{"颜色": "冰霜银", "款式": "标准版"}', 1299.00, 60, 'https://picsum.photos/seed/freebuds-3/400/400', NOW()),
(15, '砚黑 标准版', '{"颜色": "砚黑", "款式": "标准版"}', 1299.00, 50, 'https://picsum.photos/seed/freebuds-4/400/400', NOW()),

-- 索尼WH-1000XM5 (product_id=16) - 3颜色 = 3个SKU
(16, '黑色', '{"颜色": "黑色"}', 2299.00, 50, 'https://picsum.photos/seed/sonyxm5-1/400/400', NOW()),
(16, '铂金银', '{"颜色": "铂金银"}', 2299.00, 40, 'https://picsum.photos/seed/sonyxm5-2/400/400', NOW()),
(16, '深夜蓝', '{"颜色": "深夜蓝"}', 2299.00, 30, 'https://picsum.photos/seed/sonyxm5-3/400/400', NOW()),

-- PlayStation 5 (product_id=17) - 2版本 x 2套装 = 4个SKU
(17, '光驱版 单主机', '{"版本": "光驱版", "套装": "单主机"}', 3599.00, 25, 'https://picsum.photos/seed/ps5-1/400/400', NOW()),
(17, '光驱版 双手柄套装', '{"版本": "光驱版", "套装": "双手柄套装"}', 3999.00, 25, 'https://picsum.photos/seed/ps5-2/400/400', NOW()),
(17, '数字版 单主机', '{"版本": "数字版", "套装": "单主机"}', 2999.00, 20, 'https://picsum.photos/seed/ps5-3/400/400', NOW()),
(17, '数字版 双手柄套装', '{"版本": "数字版", "套装": "双手柄套装"}', 3399.00, 20, 'https://picsum.photos/seed/ps5-4/400/400', NOW()),

-- Nintendo Switch OLED (product_id=18) - 2颜色 x 2套装 = 4个SKU
(18, '白色 标准版', '{"颜色": "白色", "套装": "标准版"}', 2349.00, 40, 'https://picsum.photos/seed/switch-1/400/400', NOW()),
(18, '白色 精灵宝可梦限定版', '{"颜色": "白色", "套装": "精灵宝可梦朱紫限定版"}', 2599.00, 20, 'https://picsum.photos/seed/switch-2/400/400', NOW()),
(18, '电光红-蓝 标准版', '{"颜色": "电光红-蓝", "套装": "标准版"}', 2349.00, 40, 'https://picsum.photos/seed/switch-3/400/400', NOW()),
(18, '电光红-蓝 精灵宝可梦限定版', '{"颜色": "电光红-蓝", "套装": "精灵宝可梦朱紫限定版"}', 2599.00, 20, 'https://picsum.photos/seed/switch-4/400/400', NOW()),

-- 小米扫地机器人3C+ (product_id=19) - 1个SKU
(19, '白色', '{"颜色": "白色"}', 999.00, 150, 'https://picsum.photos/seed/robot3c-1/400/400', NOW()),

-- 石头扫地机器人G20S (product_id=20) - 2颜色 = 2个SKU
(20, '黑色', '{"颜色": "黑色"}', 4999.00, 40, 'https://picsum.photos/seed/roborock-1/400/400', NOW()),
(20, '白色', '{"颜色": "白色"}', 4999.00, 40, 'https://picsum.photos/seed/roborock-2/400/400', NOW()),

-- 小米智能门锁M20 Pro (product_id=21) - 1个SKU
(21, '黑色', '{"颜色": "黑色"}', 2299.00, 90, 'https://picsum.photos/seed/smartlock-1/400/400', NOW()),

-- 三星990 PRO SSD (product_id=22) - 3容量 = 3个SKU
(22, '1TB', '{"容量": "1TB"}', 1099.00, 80, 'https://picsum.photos/seed/ssd-1/400/400', NOW()),
(22, '2TB', '{"容量": "2TB"}', 1399.00, 100, 'https://picsum.photos/seed/ssd-2/400/400', NOW()),
(22, '4TB', '{"容量": "4TB"}', 2399.00, 20, 'https://picsum.photos/seed/ssd-3/400/400', NOW()),

-- 西部数据My Passport (product_id=23) - 3颜色 x 3容量 = 9个SKU (简化取3个)
(23, '黑色 2TB', '{"颜色": "黑色", "容量": "2TB"}', 499.00, 60, 'https://picsum.photos/seed/wdpassport-1/400/400', NOW()),
(23, '黑色 4TB', '{"颜色": "黑色", "容量": "4TB"}', 699.00, 60, 'https://picsum.photos/seed/wdpassport-2/400/400', NOW()),
(23, '黑色 5TB', '{"颜色": "黑色", "容量": "5TB"}', 799.00, 60, 'https://picsum.photos/seed/wdpassport-3/400/400', NOW()),

-- Anker 100W充电器 (product_id=24) - 2颜色 = 2个SKU
(24, '黑色', '{"颜色": "黑色"}', 199.00, 250, 'https://picsum.photos/seed/anker100w-1/400/400', NOW()),
(24, '白色', '{"颜色": "白色"}', 199.00, 250, 'https://picsum.photos/seed/anker100w-2/400/400', NOW()),

-- 小米移动电源3 (product_id=25) - 1个SKU
(25, '银色', '{"颜色": "银色"}', 149.00, 400, 'https://picsum.photos/seed/powerbank-1/400/400', NOW());

-- 场景数据
INSERT INTO `scenarios` (`name`, `description`, `cover_url`, `config_data`, `created_at`) VALUES
('学生开学季', '新学期新装备，数码好物助力学业，精选平板、笔记本、学习配件专属优惠', 'https://picsum.photos/seed/student/800/400', '{"tags": ["学生优惠", "限时抢购"], "banner_color": "#4A90D9"}', NOW()),
('职场精英必备', '提升办公效率，精选高性能轻薄本、智能手表、无线耳机，打造高效职场生活', 'https://picsum.photos/seed/office/800/400', '{"tags": ["商务人士", "高效办公"], "banner_color": "#2C3E50"}', NOW()),
('数码发烧友', '最新旗舰手机、游戏主机、高端耳机，追求极致性能的数码爱好者首选', 'https://picsum.photos/seed/geek/800/400', '{"tags": ["旗舰", "高端"], "banner_color": "#E74C3C"}', NOW()),
('智能家居生活', '扫地机器人、智能门锁、智能音箱，让家更智能，生活更便捷', 'https://picsum.photos/seed/smarthome/800/400', '{"tags": ["智能", "便捷"], "banner_color": "#27AE60"}', NOW());

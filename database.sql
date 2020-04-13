create database if not exists my_shop;
use my_shop;

/******商品分类信息*****/
drop table if exists `t_product_category`;
create table t_product_category (
  id char(20) not null, #分类id，Cellphone、Computer
  name char(50), #分类名称，如手机、电脑
  slogan varchar(200), #该分类的宣传标语，如[科技改变生活]
  display_order int, #显示顺序，数值越小，在页面中的位置越靠前
  remark varchar(200), #备注
  primary key(id)
);

/******品牌信息*****/
drop table if exists `t_product_brand`;
create table t_product_brand (
  id char(20) not null, #品牌id，如Xiaomi、Lenovo
  name char(50), #品牌名称，如小米、联想
  rank_order int, #排名顺序，数值越小，在页面中的位置越靠前
  remark varchar(200), #备注
  primary key(id)
);

/******商品信息*****/
drop table if exists `t_product`;
create table t_product (
  id int not null, #商品id
  category_id char(20), #商品分类id，即t_product_category中的id
  brand_id char(20), #品牌id，即t_product_brand中的id
  name char(50), #商品名称，如iphone 10 plus
  price decimal(10, 2), #售价
  sale_price decimal(10, 2), #促销价
  purchase_amount int, #进货数量
  stock_balance int, #库存数量
  listing_date date, #商品上市日期
  description varchar(200), #商品描述
  remark varchar(200), #备注
  primary key(id)
);

/******商品图标路径信息*****/
drop table if exists `t_product_images`;
create table t_product_images (
  id int not null, #商品id，即t_product中的id
  sequence int not null, #顺序，数值越小，显示越靠前
  type char(10), #图片类型，cover - 封面图（一个商品一般只有一张），exhibit - 展示图（一个商品一般有多张）
  path char(100), #图片所在路径，/pimages/uuid_name.jpg
  original_name char(100), #图片文件原来的名称
  remark varchar(200), #备注
  primary key(id, sequence)
);

/******用户信息*****/
drop table if exists `t_user`;
create table t_user (
  type char(10), #类别，user - 普通用户，admin - 管理员
  account char(20) not null, #账号，如mouse、peter
  name char(20), #姓名
  password char(20), #密码
  sex char(1), #性别
  email char(30), #邮箱
  cellphone char(15), #手机
  primary key(type, account)
);

/******购物车*****/
drop table if exists `t_cart`;
create table t_cart (
  account char(20) not null, #账号，即t_user中的account
  product_id int not null, #商品id，即t_product中的id
  amount int, #数量
  primary key(account, product_id)
);

/******广告*****/
drop table if exists `t_advertisement`;
create table t_advertisement (
  type char(10) not null, #广告大类，slide - 轮播广告，category - 商品分类广告
  subtype char(20) not null, #广告子类
  id int not null, #该类型广告的id
  product_id int, #商品id，即t_product中的id
  remark varchar(200), #备注
  primary key(type, subtype, id)
);

/******定单*****/
drop table if exists `t_order`;
create table t_order (
  id int not null, #定单id
  account char(20) not null, #账号，即t_user中的account
  commit_date date, #定单提交日期
  status char(10), #定单状态，ordered - 已下单未支付，payed - 已支持，done - 完成
  primary key(id, account)
);

/******定单商品信息*****/
drop table if exists `t_order_products`;
create table t_order_products (
  id int not null, #定单id，即t_order中的id
  product_id int not null, #商品id，即t_product中的id
  price decimal(10, 2), #成交价格
  amount int, #购买数量
  primary key(id, product_id)
);

insert into t_product_brand values ('Apple', '苹果', 4, null);
insert into t_product_brand values ('Asus', '华硕', 7, null);
insert into t_product_brand values ('Dell', '戴尔', 5, null);
insert into t_product_brand values ('Hp', '惠普', 10, null);
insert into t_product_brand values ('Huawei', '华为', 3, null);
insert into t_product_brand values ('Lenovo', '联想', 2, null);
insert into t_product_brand values ('Meizu', '魅族', 8, null);
insert into t_product_brand values ('Oneplus', '一加', 9, null);
insert into t_product_brand values ('Samsung', '三星', 6, null);
insert into t_product_brand values ('Xiaomi', '小米', 1, null);

insert into t_product_category values ('Accessory', '配件', '丰富生活日常', '/images/index/phone/phone-left.jpg', 5, null);
insert into t_product_category values ('Cellphone', '手机', '让我们变得更近', '/images/index/phone/phone-left.jpg', 2, null);
insert into t_product_category values ('Computer', '电脑', '科技改变生活', '/images/index/computer/computer-left.jpg', 1, null);
insert into t_product_category values ('HomeAppliance', '家电', '成为你的必需', '/images/index/家电/appliance-left.jpg', 3, null);
insert into t_product_category values ('Life', '生活', '生活更有滋味', '/images/index/phone/phone-left.jpg', 6, null);
insert into t_product_category values ('Wearing', '穿戴', '为生活增添乐趣', '/images/index/phone/phone-left.jpg', 4, null);

insert into t_product values (1, 'Cellphone', 'Xiaomi', 'Redmi Note8 Pro', 1299.00, 1099.00, 1000, 123, '2020-01-01', '', null);
insert into t_product values (2, 'HomeAppliance', 'Xiaomi', '小米电视', 8999.00, 7999.00, 1000, 231, '2020-01-02', '', null);
insert into t_product values (3, 'Computer', 'Xiaomi', '小米电脑', 4299.00, 4199.00, 1000, 321, '2020-01-03', '', null);
insert into t_product values (4, 'HomeAppliance', 'Xiaomi', '小米洗衣机', 1999.00, 1900.00, 1000, 300, '2020-01-04', '', null);
insert into t_product values (5, 'HomeAppliance', 'Xiaomi', '小米空调', 1799.00, 1700.00, 1000, 111, '2020-01-05', '', null);
insert into t_product values (6, 'Computer', 'Xiaomi', '小米Pro 15.6" GTX显卡', 6299.00, 6000.00, 1000, 136, '2020-01-06', '全新第八代英特尔酷睿处理器 ／ 升级金属双风扇 ／ 带宽提升80% ／ 15.6"全高清大屏超窄边大视野', null);
insert into t_product values (7, 'Computer', 'Lenovo', 'Thinkbook 13s', 4299.00, 4199.00, 1000, 400, '2020-01-06', '', null);
insert into t_product values (8, 'Computer', 'Lenovo', 'ThinkBook 14s', 5299.00, 5099.00, 1000, 99, '2020-01-07', '', null);
insert into t_product values (9, 'Computer', 'Lenovo', 'ThinkPad T490', 13999.00, 10999.00, 1000, 192, '2020-01-08', '', null);
insert into t_product values (10, 'Computer', 'Lenovo', 'ThinkPad X1 Yoga', 16899.00, 16000.00, 1000, 399, '2020-01-09', '', null);
insert into t_product values (11, 'Computer', 'Lenovo', '一体机', 23499.00, 20499.00, 1000, 765, '2020-01-10', '', null);

insert into t_product_images values (1, 1, 'thumbnail', '/pimages/27335c1d54514f1cbe3cdfcd4a07a69d.jpg', 'Redmi Note8 Pro 1299.jpg', null);
insert into t_product_images values (2, 1, 'thumbnail', '/pimages/2dab3d681934428f8cdbe8aa2a1ff2d2.jpg', '小米 4-75 75寸 8999.jpg', null);
insert into t_product_images values (3, 1, 'thumbnail', '/pimages/95aaaac62b2347158f0c854aa463f910.jpg', '15.6极限版 4299.jpg', null);
insert into t_product_images values (4, 1, 'thumbnail', '/pimages/d19277ed65304fabb0dba3c3bc40f271.jpg', '米家变频10公斤滚桶烘干 1999.jpg', null);
insert into t_product_images values (5, 1, 'thumbnail', '/pimages/240034181201460a9a726fa0c5185fca.jpg', '米家1.5P 1799.jpg', null);
insert into t_product_images values (6, 1, 'thumbnail', '/pimages/4b7d1d1ebf874e81a2f42dcb39f0a07d.jpg', '15.6 GTX显卡 6299.jpg', null);
insert into t_product_images values (6, 2, 'exhibit', '/pimages/406aadc615ac449f9e668511ab535465.jpg', 'img1.jpg', null);
insert into t_product_images values (6, 3, 'exhibit', '/pimages/91201874980747caac791bf5bc26325d.jpg', 'img2.jpg', null);
insert into t_product_images values (6, 4, 'exhibit', '/pimages/6d85fb405fd540e9a1c7e283f04d6ac0.png', 'img3.png', null);
insert into t_product_images values (6, 5, 'exhibit', '/pimages/8fdb8fbcc5844f77b91003224db57069.png', 'img4.png', null);
insert into t_product_images values (6, 6, 'exhibit', '/pimages/9c4de87e751c476d87b5c8a53b14d577.png', 'img5.png', null);
insert into t_product_images values (6, 7, 'exhibit', '/pimages/d189440e989e46c3b2a6948e47d58e8a.jpg', 'img6.jpg', null);
insert into t_product_images values (6, 8, 'exhibit', '/pimages/32f1a0114caf4f31b31ed582cefadf4e.jpg', '参数.jpg', null);
insert into t_product_images values (7, 1, 'thumbnail', '/pimages/19257ceddb4e4927b16db8f19da14385.jpg', 'Thinkbook 13s 4299.jpg', null);
insert into t_product_images values (8, 2, 'thumbnail', '/pimages/335adb1dab3644bab5a34a4b71e7a88a.jpg', 'ThinkBook 14s 5299.jpg', null);
insert into t_product_images values (9, 1, 'thumbnail', '/pimages/ae232628104e4276b559bca36e62c68d.jpg', 'ThinkPad T490 13999.jpg', null);
insert into t_product_images values (10, 1, 'thumbnail', '/pimages/9b005897ba9042f6bb399e61fa87afe3.jpg', 'ThinkPad X1 Yoga.jpg', null);
insert into t_product_images values (11, 1, 'thumbnail', '/pimages/2dd4bf57fe2f41029231ca276b6186c9.jpg', '一体机.jpg', null);

insert into t_cart values ('mouse', 1, 2);
insert into t_cart values ('mouse', 2, 4);


insert into t_order values (1, 'mouse', '2020-02-28', 'done');
insert into t_order values (2, 'mouse', '2020-03-19', 'payed');

insert into t_order_products values (1, 3, 199.00, 3);
insert into t_order_products values (1, 4, 299.00, 2);
insert into t_order_products values (2, 5, 399.00, 4);
insert into t_order_products values (2, 6, 499.00, 1);
insert into t_order_products values (2, 7, 599.00, 5);
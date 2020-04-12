create database if not exists my_shop;
use my_shop;

/******商品分类信息*****/
drop table if exists `t_product_category`;
create table t_product_category (
  id char(20) not null, #分类id，Cellphone、Computer
  name char(50), #分类名称，如手机、电脑
  ad_words varchar(200), #该分类的广告语，如[科技改变生活]
  ad_image char(100), #该分类的广告图片路径
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
drop table if exists `t_product_image`;
create table t_product_image (
  id int not null, #商品id，即t_product中的id
  sequence int not null, #顺序，数值越小，显示越靠前
  type char(10), #图片类型，thumbnail - 缩略图（一个商品一般只有一张），exhibit - 展示图（一个商品一般有多张）
  path char(100), #图片所在路径
  remark varchar(200), #备注
  primary key(id, sequence)
);

/******用户信息*****/
drop table if exists `t_user`;
create table t_user (
  account char(20) not null, #账号，如mouse、peter
  password char(20), #密码
  name char(20), #姓名
  sex char(1), #性别
  primary key(account)
);

/******购物车*****/
drop table if exists `t_cart`;
create table t_cart (
  account char(20) not null, #账号，即t_user中的account
  product_id int not null, #商品id，即t_product中的id
  amount int, #数量
  primary key(account, product_id)
);

/******收藏*****/
drop table if exists `t_collection`;
create table t_collection (
  account char(20) not null, #账号，即t_user中的account
  product_id int not null, #商品id，即t_product中的id
  primary key(account, product_id)
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

insert into t_product values (1, 'Cellphone', 'Xiaomi', 'Redmi Note8 Pro', 1299.00, 1099.00, 1000, 123, '2020-01-01', null, null);
insert into t_product values (2, 'HomeAppliance', 'Xiaomi', '小米电视', 8999.00, 7999.00, 1000, 231, '2020-01-02', null, null);
insert into t_product values (3, 'Computer', 'Xiaomi', '小米电脑', 4299.00, 4199.00, 1000, 321, '2020-01-03', null, null);
insert into t_product values (4, 'HomeAppliance', 'Xiaomi', '小米洗衣机', 1999.00, 1900.00, 1000, 300, '2020-01-04', null, null);
insert into t_product values (5, 'HomeAppliance', 'Xiaomi', '小米空调', 1799.00, 1700.00, 1000, 111, '2020-01-05', null, null);
insert into t_product values (6, 'Computer', 'Xiaomi', '小米Pro 15.6" GTX显卡', 1799.00, 1700.00, 1000, 111, '2020-01-05', '全新第八代英特尔酷睿处理器 ／ 升级金属双风扇 ／ 带宽提升80% ／ 15.6"全高清大屏超窄边大视野', null);
insert into t_product values (7, 'Computer', 'Lenovo', 'Thinkbook 13s', 4299.00, 4199.00, 1000, 400, '2020-01-06', null, null);
insert into t_product values (8, 'Computer', 'Lenovo', 'ThinkBook 14s', 5299.00, 5099.00, 1000, 99, '2020-01-07', null, null);
insert into t_product values (9, 'Computer', 'Lenovo', 'ThinkPad T490', 13999.00, 10999.00, 1000, 192, '2020-01-08', null, null);
insert into t_product values (10, 'Computer', 'Lenovo', 'ThinkPad X1 Yoga', 16899.00, 16000.00, 1000, 399, '2020-01-09', null, null);
insert into t_product values (11, 'Computer', 'Lenovo', '一体机', 23499.00, 20499.00, 1000, 765, '2020-01-10', null, null);

insert into t_product_image values (1, 0, 'thumbnail', '/images/index/phone/mi/Redmi Note8 Pro 1299.jpg', null);
insert into t_product_image values (2, 0, 'thumbnail', '/images/index/家电/mi/电视/小米 4-75 75寸 8999.jpg', null);
insert into t_product_image values (3, 0, 'thumbnail', '/images/index/computer/mi/15.6极限版 4299.jpg', null);
insert into t_product_image values (4, 0, 'thumbnail', '/images/index/家电/mi/洗衣机/米家变频10公斤滚桶烘干 1999.jpg', null);
insert into t_product_image values (5, 0, 'thumbnail', '/images/index/家电/mi/空调/米家1.5P 1799.jpg', null);
insert into t_product_image values (6, 0, 'thumbnail', '/images/index/computer/mi/15.6 GTX显卡 6299.jpg', null);
insert into t_product_image values (6, 1, 'exhibit', '/images/mi/computer/Pro 15.6增强版/img1.jpg', null);
insert into t_product_image values (6, 2, 'exhibit', '/images/mi/computer/Pro 15.6增强版/img2.jpg', null);
insert into t_product_image values (6, 3, 'exhibit', '/images/mi/computer/Pro 15.6增强版/img3.png', null);
insert into t_product_image values (6, 4, 'exhibit', '/images/mi/computer/Pro 15.6增强版/img4.png', null);
insert into t_product_image values (6, 5, 'exhibit', '/images/mi/computer/Pro 15.6增强版/img5.png', null);
insert into t_product_image values (6, 6, 'exhibit', '/images/mi/computer/Pro 15.6增强版/img6.jpg', null);
insert into t_product_image values (6, 7, 'exhibit', '/images/mi/computer/Pro 15.6增强版/参数.jpg', null);
insert into t_product_image values (7, 0, 'thumbnail', '/images/index/computer/lenovo/Thinkbook 13s 4299.jpg', null);
insert into t_product_image values (8, 0, 'thumbnail', '/images/index/computer/lenovo/ThinkBook 14s 5299.jpg', null);
insert into t_product_image values (9, 0, 'thumbnail', '/images/index/computer/lenovo/ThinkPad T490 13999.jpg', null);
insert into t_product_image values (10, 0, 'thumbnail', '/images/index/computer/lenovo/ThinkPad X1 Yoga.jpg', null);
insert into t_product_image values (11, 0, 'thumbnail', '/images/index/computer/lenovo/一体机.jpg', null);

insert into t_cart values ('mouse', 1, 2);
insert into t_cart values ('mouse', 2, 4);


insert into t_order values (1, 'mouse', '2020-02-28', 'done');
insert into t_order values (2, 'mouse', '2020-03-19', 'payed');

insert into t_order_products values (1, 3, 199.00, 3);
insert into t_order_products values (1, 4, 299.00, 2);
insert into t_order_products values (2, 5, 399.00, 4);
insert into t_order_products values (2, 6, 499.00, 1);
insert into t_order_products values (2, 7, 599.00, 5);
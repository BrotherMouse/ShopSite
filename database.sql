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

/******测试数据*****/
insert into t_product_brand values ('apple', '苹果', 4, null);
insert into t_product_brand values ('asus', '华硕', 7, null);
insert into t_product_brand values ('dell', '戴尔', 5, null);
insert into t_product_brand values ('hp', '惠普', 10, null);
insert into t_product_brand values ('huawei', '华为', 3, null);
insert into t_product_brand values ('lenovo', '联想', 2, null);
insert into t_product_brand values ('meizu', '魅族', 8, null);
insert into t_product_brand values ('oneplus', '一加', 9, null);
insert into t_product_brand values ('samsung', '三星', 6, null);
insert into t_product_brand values ('xiaomi', '小米', 1, null);

insert into t_product_category values ('accessory', '配件', '丰富生活日常', 5, null);
insert into t_product_category values ('cellphone', '手机', '让我们变得更近', 2, null);
insert into t_product_category values ('computer', '电脑', '科技改变生活', 1, null);
insert into t_product_category values ('appliance', '家电', '成为你的必需', 3, null);
insert into t_product_category values ('life', '生活', '生活更有滋味', 6, null);
insert into t_product_category values ('wearing', '穿戴', '为生活增添乐趣', 4, null);

insert into t_cart values ('mouse', 1, 2);
insert into t_cart values ('mouse', 2, 4);


insert into t_order values (1, 'mouse', '2020-02-28', 'done');
insert into t_order values (2, 'mouse', '2020-03-19', 'payed');

insert into t_order_products values (1, 3, 199.00, 3);
insert into t_order_products values (1, 4, 299.00, 2);
insert into t_order_products values (2, 5, 399.00, 4);
insert into t_order_products values (2, 6, 499.00, 1);
insert into t_order_products values (2, 7, 599.00, 5);
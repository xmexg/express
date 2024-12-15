# 校园快递帮微信小程序  
前身是废弃的 https://github.com/xmexg/wkexpress  
现拿来继续开发当作软工结课大作业和jsp结课大作业(可能还是我未来的毕业设计项目)  

# 功能介绍
- 原始 springboot + 微信小程序

|||||
|:--:|:--:|:--:|:--:|
|![首页](./image/1.png)|![下单](./image/2.png)|![个人主页](./image/3.png)|![订单列表](./image/4.png)|
|首页|下单|个人主页|订单列表|
|![未支付下单](./image/5.png)|![模拟支付](./image/6.png)|![查看订单](./image/7.png)|![查看派单](./image/8.png)|
|未支付下单|模拟支付|查看订单|查看派单|
|![普通用户主页](./image/9.png)|![普通用户订单](./image/10.png)|![用户取消订单](./image/11.png)|![关于](./image/12.png)|
|普通用户主页|普通用户订单|用户取消订单|关于|

- 补jsp作业

|||||
|--|--|--|--|
|![识别码登录网页端](./image/21.png)||||
# 开始使用

## 数据库配置  
 + 自行创建数据库schoolexpress  
 + 查看数据库  
```sql
show databases;
```
 + 使用schoolexpress数据库  
```sql
use schoolexpress;
```
 + 创建configdata数据表,用于存放一些基本的配置信息  
```sql
create table if not exists configdata(`name` varchar(50), `value` varchar(50))engine=InnoDB default charset=utf8;
```
 + 创建expresspoint数据表,用于存放快递点  
```sql
create table if not exists expresspoint(`point` varchar(50))engine=InnoDB default charset=utf8;
```
 + 创建userlist数据表,用于存放用户列表,user_type:1配送员,2用户(默认),9管理员  
```sql
create table if not exists userlist (`user_id` int unsigned auto_increment, `user_type` tinyint, `user_session_key` varchar(50), `user_openid` varchar(50), `user_token` varchar(100), `user_creattime` datetime, primary key (`user_id`))engine=InnoDB default charset=utf8;
```
 + 创建booking数据表,用于存放当前正在进行的订单,包含已支付的和未支付的  
 openid:支付者的微信唯一标识,orderid:服务器生成的订单号,ordertime:用户下单时间,ordertype:下单类型(0:用户确认价格后取货,1:直接取货),orderpricecon:用户是否已确认订单价格(0:未确认,1:已确认或不需确认),orderamount:订单金额,pickup:取货地点,pickdown:送达地点,pickcode:取货码,pickname:包裹名,name:姓名,phone:手机号,note:备注,shipper送货员的openid,payid:微信支付订单号,paytime:用户付款的时间, status订单状态,1:已下单,2:已接单,11:已完成,12:已取消
```sql
CREATE TABLE `booking` (
  `openid` varchar(50) DEFAULT NULL,
  `orderid` varchar(50) DEFAULT NULL,
  `ordertime` datetime DEFAULT NULL,
  `ordertype` tinyint(4) DEFAULT NULL,
  `orderpricecon` tinyint(4) DEFAULT NULL,
  `orderamount` double DEFAULT NULL,
  `pickup` varchar(50) DEFAULT NULL,
  `pickdown` varchar(50) DEFAULT NULL,
  `pickcode` varchar(20) DEFAULT NULL,
  `pickname` varchar(20) DEFAULT NULL,
  `name` varchar(10) DEFAULT NULL,
  `phone` varchar(20) DEFAULT NULL,
  `note` varchar(100) DEFAULT NULL,
  `shipper` varchar(50) DEFAULT NULL,
  `payid` varchar(50) DEFAULT NULL,
  `paytime` datetime DEFAULT NULL,
  `status` int(11) NOT NULL DEFAULT '1' COMMENT '订单状态',
  `changepriceable` int(11) NOT NULL DEFAULT '0' COMMENT '1能0否改变价格',
  KEY `status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
```

 + 创建schoolnet数据表，用于存放办理校园网的信息，其中id:主键,name:学生姓名,phone:学生手机号,stuid:学号,idcard:身份证号,department:院系,duration,办理时长,orderdate:订单创建时间,state:办理状态(暂时无用,0:未付款,1:已付款,2:已取消),money:办理金额

```sql
create table if not exists `schoolnet`(`id` int unsigned auto_increment, openid varchar(50) not null, name varchar(10) not null, phone varchar(20) not null, stuid varchar(20) not null, idcard varchar(20), department varchar(50), duration varchar(10) not null, orderdate timestamp, state tinyint, money double, primary key (`id`)) ENGINE=InnoDB DEFAULT CHARSET=utf8;
```

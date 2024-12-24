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

|||
|--|--|
|![识别码登录网页端](./image/21.png)|![jsp配送员](./image/jsp-1.png)|
|![jsp管理员](./image/jsp-2.png)|![jsp关于](./image/jsp-3.png)|
|![jsp配送员反馈](./image/jsp-4.png)|![jsp管理员回复](./image/jsp-5.png)|
|![jsp登录](./image/jsp-10.png)|![jsp404](./image/jsp-11.png)|

- 思维导图
![思维导图.png](./image/思维导图.png)

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

### 扩展web专有表

+ 创建反馈表  
id：主键，自动递增。  
openid：标识用户，用于区分不同用户（管理员和普通用户）。  
is_admin：表示用户类型，默认值为 FALSE（普通用户）。
parent_id：用于实现父子关系。为 NULL 表示是主题帖。指向另一个记录的 id 表示评论该记录。  
content：保存反馈的正文内容。
created_at：记录创建时间，默认为当前时间。  
updated_at：记录最后一次修改时间（可选字段，可通过 UPDATE 操作更新）现在不用。  
外键约束：parent_id 引用自身的 id，并设置级联删除（即删除主题帖时，其下的评论也会被删除）。  
存储引擎和字符集：
使用 InnoDB 作为存储引擎，支持事务和外键。
字符集设置为 utf8mb4，支持 Emoji 和多语言。
  ```sql
  CREATE TABLE feedback (
      id BIGINT AUTO_INCREMENT PRIMARY KEY,         -- 每条记录的唯一标识
      openid VARCHAR(50) NOT NULL,                 -- 用户唯一标识 
      is_admin BOOLEAN NOT NULL DEFAULT FALSE,      -- 当时是否是管理员 
      parent_id BIGINT DEFAULT NULL,                -- 父记录的 ID，主题帖为 NULL
      content TEXT NOT NULL,                        -- 反馈内容
      created_at DATETIME NOT NULL DEFAULT NOW(),   -- 创建时间
      updated_at DATETIME DEFAULT NULL,             -- 最后修改时间，可选，现在不用
      CONSTRAINT fk_parent_feedback FOREIGN KEY (parent_id) REFERENCES feedback (id)
          ON DELETE CASCADE                          -- 父子关系的外键，支持级联删除
  ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
  ```

  示例：  
  插入一条主题帖
  ```sql
  INSERT INTO feedback (openid, is_admin, parent_id, content, created_at)
  VALUES ('user12345', FALSE, NULL, 'This is my feedback about...', NOW());
  ```
  插入一条评论（普通用户）
  ```sql
  INSERT INTO feedback (openid, is_admin, parent_id, content, created_at)
  VALUES ('user12345', FALSE, 1, 'Here is my additional comment.', NOW());
  ```
  插入管理员回复
  ```sql
  INSERT INTO feedback (openid, is_admin, parent_id, content, created_at)
  VALUES ('admin56789', TRUE, 1, 'Thank you for your feedback!', NOW());
  ```
  查询数据示例
  查看某用户的主题帖和评论：
  ```sql
  SELECT * FROM feedback 
  WHERE openid = 'user12345' OR 
        (parent_id IS NOT NULL AND parent_id IN (SELECT id FROM feedback WHERE openid = 'user12345'));
  ```
  管理员查看所有数据：
  ```sql
  SELECT * FROM feedback;
  ```
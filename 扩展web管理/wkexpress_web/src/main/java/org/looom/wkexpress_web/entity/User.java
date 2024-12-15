package org.looom.wkexpress_web.entity;

import lombok.Getter;
import lombok.Setter;

import java.sql.Date;

@Getter
public class User {

    //user_id是主键,自增
    private int user_id; //用户id
    @Setter
    private int user_type; //用户类型,9:管理员,1:配送员,2:用户,3:黑名单
    @Setter
    private String user_session_key; //微信session_key
    @Setter
    private String user_openid; //微信openid
    @Setter
    private String user_token; //用户的token
    @Setter
    private Date user_creattime; //用户注册时间

}

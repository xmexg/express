package org.xmexg.wkexpress.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.xmexg.wkexpress.dao.UserDao;
import org.xmexg.wkexpress.entity.User;
import org.xmexg.wkexpress.tools.HttpUtility;
import org.xmexg.wkexpress.tools.RandomCreate;

import java.sql.Date;

/*
 * 微信小程序登录
 * 从前端收到code,然后发送到后端,后端再发送到微信服务器,获取openid
 */
@Component
public class UserService {

    @Autowired
    private UserDao userDao;

    @Value("${wechat.appid}")
    private String appid;

    @Value("${wechat.appserect}")
    private String appsecret;

    private String grant_type = "authorization_code";//授权类型,此处只需填写authorization_code

    /**
     * 获取用户信息,包含用户id(user_id),用户类型(user_type),用户令牌(user_token)
     * openid内容是不会发送给前端的,只有session_key会发送给前端
     * 用户没有注册,则通过code注册,返回json用户信息
     * 用户已经注册,则检查信息是否正确,并返回json用户信息
     */
    public String loginByCode(String code){
        String sessionData = jscode2session(code);
        if(sessionData == null || sessionData.length() == 0){
            return null;
        }
        //去掉双引号
        sessionData = sessionData.replace("\"", "");
        //去掉首尾的大括号
        sessionData = sessionData.substring(1, sessionData.length() - 1);
        String[] sessionDataArray = sessionData.split(",");
        String wx_session_key, wx_openid;
        if(sessionDataArray[0].startsWith("session")){
            wx_session_key = sessionDataArray[0].split(":")[1];
            wx_openid = sessionDataArray[1].split(":")[1];
        }else {
            wx_session_key = sessionDataArray[1].split(":")[1];
            wx_openid = sessionDataArray[0].split(":")[1];
        }
        //通过openid查询数据库,看是否已经注册
        User user = userDao.getUserByOpenid(wx_openid);
        if(user == null) {
            //用户没有注册,则通过code注册,返回用户信息
            user = new User();
            user.setUser_openid(wx_openid);
            user.setUser_session_key(wx_session_key);
            user.setUser_creattime(new Date(System.currentTimeMillis()));
            user.setUser_type(2);//默认为普通用户
            user.setUser_token(RandomCreate.createToken());
            int userid = userDao.addUser(user);//添加用户后会返回主键,即用户id
            return "{\"user_id\":"+userid+",\"user_type\":"+user.getUser_type()+",\"user_token\":\""+user.getUser_token()+"\"}";
        }else {
            //用户已经注册,则检查信息是否正确,并返回用户信息
            if(!user.getUser_session_key().equals(wx_session_key)){
                //session_key不一致,则更新session_key
                user.setUser_session_key(wx_session_key);
                userDao.updateSessionKey(user);
            }
            return "{\"user_id\":"+user.getUser_id()+",\"user_type\":"+user.getUser_type()+",\"user_token\":\""+user.getUser_token()+"\"}";
        }
    }

    //获取用户session_key,但需要前端提供user_id和user_token
    //禁止把session_key发送给前端
//    public String getSessionKey(int id, String token){
//    }


    public User getUserByToken(String token) {
        return userDao.getUserByToken(token);
    }

    //前端获取code,在这里通过code获取openid和session_key
    private String jscode2session(String code){
        if(code == null || code.length() == 0){
            return null;
        }
        String url = "https://api.weixin.qq.com/sns/jscode2session?appid="+appid+"&secret="+appsecret+"&js_code="+code+"&grant_type="+grant_type;
        System.out.println("连接信息:"+url);
        //发送get请求到https://api.weixin.qq.com/sns/jscode2session
        //获取openid和session_key
        String result = HttpUtility.doGet(url);
        if(result != null && result.length() > 0){
            return result;
        } else {
            return null;
        }
    }

    // 修改用户类型
    public boolean changeUserType(String openid, int type){
        return userDao.changeUserType(openid, type) > 0;
    }

}

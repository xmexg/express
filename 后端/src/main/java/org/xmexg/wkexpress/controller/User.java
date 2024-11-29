package org.xmexg.wkexpress.controller;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.xmexg.wkexpress.service.UserService;
import org.xmexg.wkexpress.tools.MySqlTools;
import org.xmexg.wkexpress.tools.ResponseModel;

@RestController
@RequestMapping("/user")
public class User {

    @Autowired
    private UserService login;
    @Autowired
    UserService userService;
    @Autowired
    MySqlTools mysqltools;
    @Autowired
    ResponseModel responseModel;

    //微信小程序登录,从前端收到code,然后发送到后端,后端再发送到微信服务器,获取openid
    @RequestMapping("/login")
    public String login(String code) {
        String result = login.loginByCode(code);
        System.out.println(code);
        System.out.println(result);
        return result;
    }

    // 获取用户信息
    @PostMapping("/get_userInfo")
    public String get_userInfo(@RequestHeader String token) {
        if (token == null || token.isEmpty()) return responseModel.notlogin();
        token = mysqltools.TransactSQLInjection(token);
        org.xmexg.wkexpress.entity.User user = userService.getUserByToken(token);
        if (user == null) return responseModel.invaliduser();
        return responseModel.success(user);
    }

    // 更改用户职位
    @PostMapping("/change_userType")
    public String change_userType(@RequestHeader String token, @RequestBody JSONObject data) {
        if (token == null || token.isEmpty()) return responseModel.notlogin();
        token = mysqltools.TransactSQLInjection(token);
        org.xmexg.wkexpress.entity.User user = userService.getUserByToken(token);
        if (user == null) return responseModel.invaliduser();
        if (user.getUser_type() != 9) return responseModel.fail("权限不足");

        String user_openid = data.getString("user_openid");
        int user_type = data.getInteger("user_type");
        if (user_openid == null || user_openid.isEmpty()) return responseModel.fail("参数错误");
        System.out.println("user_openid: " + user_openid + " user_type: " + user_type);
        return userService.changeUserType(user_openid, user_type) ? responseModel.success() : responseModel.fail();
    }
}

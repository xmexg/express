package org.xmexg.wkexpress.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.xmexg.wkexpress.entity.User;
import org.xmexg.wkexpress.service.UserService;
import org.xmexg.wkexpress.tools.MySqlTools;
import org.xmexg.wkexpress.tools.ResponseModel;

/**
 * 用于完成jsp作业，使配送员和管理员可登录网页查看订单
 */
@RestController
@RequestMapping("/webcontrol")
public class WebControl {

    @Autowired
    UserService userService;
    @Autowired
    MySqlTools mysqltools;
    @Autowired
    ResponseModel responseModel;
    /**
     * 获取识别码
     */
    @RequestMapping("/get_idcode")
    public String getIdCode(@RequestHeader String token) {
        if (token == null || token.isEmpty()) return responseModel.notlogin();
        token = mysqltools.TransactSQLInjection(token);
        User user = userService.getUserByToken(token);
        if (user == null) return responseModel.invaliduser();
        return responseModel.success(user.getUser_id());
    }

}

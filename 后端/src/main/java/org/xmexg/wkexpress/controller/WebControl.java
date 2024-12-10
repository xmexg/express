package org.xmexg.wkexpress.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.xmexg.wkexpress.WkexpressApplication;
import org.xmexg.wkexpress.entity.User;
import org.xmexg.wkexpress.service.UserService;
import org.xmexg.wkexpress.tools.MySqlTools;
import org.xmexg.wkexpress.tools.ResponseModel;

/**
 * 用于完成jsp作业，使配送员和管理员可登录网页查看订单
 */
@RestController
@RequestMapping("/webControl")
public class WebControl {

    @Autowired
    UserService userService;
    @Autowired
    MySqlTools mysqltools;
    @Autowired
    ResponseModel responseModel;

    /**
     * 获取识别码有效时间
     */
    @PostMapping("/get_idCode_liveTime")
    public String getIdCodeLiveTime() {
        return responseModel.success(WkexpressApplication.tempData.IdCode_liveTime);
    }

    /**
     * 生成识别码
     */
    @PostMapping("/get_idCode")
    public String getIdCode(@RequestHeader String token) {
        if (token == null || token.isEmpty()) return responseModel.notlogin();
        token = mysqltools.TransactSQLInjection(token);
        User user = userService.getUserByToken(token);
        if (user == null) return responseModel.invaliduser();
        return responseModel.success(WkexpressApplication.tempData.makeLoginCode(token));
    }

    /**
     * 通过识别码登录
     */
    @PostMapping("/login_by_idCode")
    public String loginByIdCode(@RequestBody String idCode) {
        if (idCode == null || idCode.isEmpty()) return responseModel.fail("参数错误");
        String token = WkexpressApplication.tempData.getLoginToken(idCode);
        if (token == null) return responseModel.fail("识别码错误");
        return responseModel.success(token);
    }

}

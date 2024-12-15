package org.looom.wkexpress_web.controller;

import com.alibaba.fastjson2.JSON;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import okhttp3.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.looom.wkexpress_web.util.ResponseModel;

import java.io.IOException;
import java.util.HashMap;

/**
 * 通过登录码登录
 */
@WebServlet(name = "Login_code", urlPatterns = "/login_code")
public class Login_code extends HttpServlet {

    // 后端主服务器地址
    private String expressServer;
    private final Logger logger = LogManager.getLogger(Login_code.class);

    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        expressServer = getServletContext().getInitParameter("expressServer");
        if(expressServer == null)  logger.error("未获取到主服务器地址,请重新配置web.xml");
        else logger.info("主服务器地址:{}", expressServer);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");  // 设置编码
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");

        String idCode = request.getParameter("loginCode");
        logger.info("获取前端值: {}", idCode);
        if (idCode.isEmpty()) {
            response.getWriter().write(ResponseModel.fail("登录码无效"));
            return;
        }
        // 向expressServer/webControl/login_by_idCode 发起post, 后台服务器通过@RequestBody String idCode接收idCode, 返回一串json
        // 准备 POST 请求数据
        OkHttpClient client = new OkHttpClient();
        MediaType JSON_TYPE = MediaType.get("application/json; charset=utf-8");
        HashMap<String, String> json_hash = new HashMap<>();  // 将 idCode 放入 JSON 格式
        json_hash.put("idCode", idCode);
        String json = JSON.toJSONString(json_hash);
        if(json == null) {
            response.getWriter().write(ResponseModel.fail("无法访问身份码"));
            return;
        }

        // 创建请求体
        RequestBody body = RequestBody.create(json, JSON_TYPE);

        // 创建请求
        Request requestObj = new Request.Builder()
                .url("http://"+expressServer+"/webControl/login_by_idCode")  // 后台服务器的 URL
                .post(body)  // 使用 POST 请求
                .build();

        // 发送请求并获取响应
        try (Response serverResponse = client.newCall(requestObj).execute()) {
            // 获取响应内容
            String responseBody = serverResponse.body().string();

            logger.info("获取主服务器登录码值：{}", responseBody);
            // 将响应内容写回客户端
            response.getWriter().write(responseBody);
        } catch (IOException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write(ResponseModel.fail("无法连接主服务器"));
        }
    }
}

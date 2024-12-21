package org.looom.wkexpress_web.controller

import jakarta.servlet.ServletConfig
import jakarta.servlet.annotation.WebServlet
import jakarta.servlet.http.HttpServlet
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.looom.wkexpress_web.entity.User
import org.looom.wkexpress_web.model.EnvModel
import org.looom.wkexpress_web.server.UserServer

/**
 * web管理页跳转
 */
@WebServlet("/web_ui")
class web_ui : HttpServlet() {

    override fun init(conf: ServletConfig) {
        super.init(conf)
    }

    override fun doPost(req: HttpServletRequest?, resp: HttpServletResponse?) {
        super.doGet(req, resp)
    }

    override fun doGet(req: HttpServletRequest?, resp: HttpServletResponse?) {
        if (req == null || resp == null) return

        // 设置编码
        req.characterEncoding = "UTF-8"
        resp.contentType = "text/html;charset=UTF-8"

        // 获取 token
        val token = req.cookies?.find { it.name == "token" }?.value
        val user: User? = UserServer.getUser(token)
        if (user == null) {
            resp.sendRedirect("login.jsp")
            return
        }

        // 将用户信息存入 req
        req.setAttribute("user", user)

        val env: HashMap<Any, Any> = EnvModel.env
        println("web_ui: ${env["web_ui"]} - ${user.user_openid} 登录成功")
        // 获取环境变量并决定跳转页面
        val pageJump = when (env["web_ui"]) {
            "express_ui" -> resp.sendRedirect("express_ui/index.html")
            else -> req.getRequestDispatcher("traditional/App.jsp").forward(req, resp)
        }
    }
}
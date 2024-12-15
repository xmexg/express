package org.looom.wkexpress_web.controller.user

import jakarta.servlet.annotation.WebServlet
import jakarta.servlet.http.HttpServlet
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.looom.wkexpress_web.server.AuthenticateServer
import org.looom.wkexpress_web.server.UserServer
import org.looom.wkexpress_web.util.ResponseModel

/**
 * 用户相关 - 获取所有用户
 */
@WebServlet("/user/listAllUsers")
class ListAllUsers : HttpServlet(){

    override fun doPost(req: HttpServletRequest?, resp: HttpServletResponse?) {
        if (req == null || resp == null) return
        val authentication = AuthenticateServer.isMANAGER(req.getAttribute("token").toString())
        if (!authentication) return resp.writer.write(ResponseModel.fail("鉴权失败"))
        return resp.writer.write(ResponseModel.success(UserServer.getAllUser()))
    }

    override fun doGet(req: HttpServletRequest?, resp: HttpServletResponse?) {
        doPost(req, resp)
    }
}
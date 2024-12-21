package org.looom.wkexpress_web.controller.exprsspoint

import com.alibaba.fastjson2.JSON
import jakarta.servlet.annotation.WebServlet
import jakarta.servlet.http.HttpServlet
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.looom.wkexpress_web.server.AuthenticateServer
import org.looom.wkexpress_web.server.ExpressPointServer
import org.looom.wkexpress_web.util.FilterCookie
import org.looom.wkexpress_web.util.ResponseModel

@WebServlet("/expresspoint/setpoint")
class SetPoint : HttpServlet() {


    override fun doGet(req: HttpServletRequest?, resp: HttpServletResponse?) {
        if (req == null || resp == null) return
        req.setCharacterEncoding("UTF-8")
        resp.setCharacterEncoding("UTF-8")
        val authentication = AuthenticateServer.isManager(FilterCookie.getToken(req))
        if (!authentication) return resp.writer.write(ResponseModel.fail("鉴权失败"))
        // 获取请求体，解析json
        val body = req.reader.readText()
        println("setpoint: $body")
        val pointList: List<String>? = JSON.parseArray(body, String::class.java)
        val status = ExpressPointServer.resetPoint(pointList)
        if (status) {
            resp.writer.write(ResponseModel.success("设置成功"))
        } else {
            resp.writer.write(ResponseModel.fail("设置失败"))
        }
    }

    override fun doPost(req: HttpServletRequest?, resp: HttpServletResponse?) {
        doGet(req, resp)
    }
}
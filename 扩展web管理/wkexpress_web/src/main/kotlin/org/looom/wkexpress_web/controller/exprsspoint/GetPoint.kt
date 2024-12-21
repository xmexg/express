package org.looom.wkexpress_web.controller.exprsspoint

import jakarta.servlet.annotation.WebServlet
import jakarta.servlet.http.HttpServlet
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.looom.wkexpress_web.server.ExpressPointServer
import org.looom.wkexpress_web.util.ResponseModel

@WebServlet("/expresspoint/getPoint")
class GetPoint : HttpServlet() {

    override fun doGet(req: HttpServletRequest?, resp: HttpServletResponse?) {
        if (req == null || resp == null) return
        req.setCharacterEncoding("UTF-8")
        resp.setCharacterEncoding("UTF-8")
        // 获取快递点接口不用鉴权
        val pointList = ExpressPointServer.getAllPoints()
        resp.writer.write(ResponseModel.success(pointList))
    }

    override fun doPost(req: HttpServletRequest?, resp: HttpServletResponse?) {
        if (req == null || resp == null) return
        req.setCharacterEncoding("UTF-8")
        resp.setCharacterEncoding("UTF-8")
        doGet(req, resp)
    }
}
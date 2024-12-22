package org.looom.wkexpress_web.controller.feedback

import jakarta.servlet.annotation.WebServlet
import jakarta.servlet.http.HttpServlet
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.looom.wkexpress_web.server.AuthenticateServer
import org.looom.wkexpress_web.server.FeedbackServer
import org.looom.wkexpress_web.util.FilterCookie
import org.looom.wkexpress_web.util.ResponseModel

/**
 * 管理员获取所有反馈
 */
@WebServlet("/feedback/getAllFeedbacks")
class GetAllFeedbacks : HttpServlet() {

    override fun doGet(req: HttpServletRequest?, resp: HttpServletResponse?) {
        if (req == null || resp == null) return
        resp.characterEncoding = "UTF-8"
        val token: String? = FilterCookie.getToken(req)
        if (token == null) {
            resp.writer.write(ResponseModel.fail("无效请求"))
            return
        }
        if (!AuthenticateServer.isManager(token)){
            resp.writer.write(ResponseModel.fail("鉴权失败"))
        }
        resp.writer.write(ResponseModel.success(FeedbackServer.getAllFeedbacks()))
    }

    override fun doPost(req: HttpServletRequest?, resp: HttpServletResponse?) {
        this.doGet(req, resp)
    }
}
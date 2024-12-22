package org.looom.wkexpress_web.controller.feedback

import jakarta.servlet.annotation.WebServlet
import jakarta.servlet.http.HttpServlet
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.looom.wkexpress_web.entity.User
import org.looom.wkexpress_web.server.FeedbackServer
import org.looom.wkexpress_web.server.UserServer
import org.looom.wkexpress_web.util.FilterCookie
import org.looom.wkexpress_web.util.ResponseModel

/**
 * 通过token获取我的反馈
 */
@WebServlet("/feedback/getMyFeedbacks")
class GetMyFeedbacks : HttpServlet(){

    override fun doGet(req: HttpServletRequest?, resp: HttpServletResponse?) {
        if (req == null || resp == null) return
        resp.characterEncoding = "UTF-8"
        val token: String? = FilterCookie.getToken(req)
        if (token == null) {
            resp.writer.write(ResponseModel.fail("鉴权失败"))
            return
        }
        val user: User? = UserServer.getUser(token)
        if (user == null) {
            resp.writer.write(ResponseModel.fail("用户不存在"))
            return
        }
        val resJson = FeedbackServer.getFeedbacksByOpenid(user.user_openid)
        println(resJson)
        resp.writer.write(ResponseModel.success(resJson))
    }

    override fun doPost(req: HttpServletRequest?, resp: HttpServletResponse?) {
        this.doGet(req, resp)
    }
}
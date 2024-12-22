package org.looom.wkexpress_web.controller.feedback

import jakarta.servlet.annotation.WebServlet
import jakarta.servlet.http.HttpServlet
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.looom.wkexpress_web.server.FeedbackServer
import org.looom.wkexpress_web.util.FilterCookie
import org.looom.wkexpress_web.util.ResponseModel

@WebServlet("/feedback/createFeedback")
class CreateFeedback : HttpServlet() {
    private val logger: Logger = LogManager.getLogger(CreateFeedback::class.java)

    override fun doGet(req: HttpServletRequest?, resp: HttpServletResponse?) {
        if (req == null || resp == null) return
        req.characterEncoding = "UTF-8"
        resp.characterEncoding = "UTF-8"
        val token: String? = FilterCookie.getToken(req)
        val content: String = req.getParameter("content") ?: return resp.writer.write(ResponseModel.fail("内容为空"))
        val parent_id: Int? = req.getParameter("parent_id")?.toIntOrNull()
        logger.info("收到前端反馈: token: $token, content: $content, parent_id: $parent_id")
        if (FeedbackServer.createFeedback(token, parent_id, content)) {
            resp.writer.write(ResponseModel.success("提交成功"))
        } else {
            resp.writer.write(ResponseModel.fail("提交失败"))
        }

    }

    override fun doPost(req: HttpServletRequest?, resp: HttpServletResponse?) {
        this.doGet(req, resp)
    }
}
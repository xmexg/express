package org.looom.wkexpress_web.controller.order

import jakarta.servlet.annotation.WebServlet
import jakarta.servlet.http.HttpServlet
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.looom.wkexpress_web.server.AuthenticateServer
import org.looom.wkexpress_web.server.OrderServer
import org.looom.wkexpress_web.util.FilterCookie
import org.looom.wkexpress_web.util.ResponseModel

@WebServlet("/order/ListMyOrders")
class ListMyOrders : HttpServlet() {
    override fun doPost(req: HttpServletRequest?, resp: HttpServletResponse?) {
        if (req == null || resp == null) return
        val token: String = FilterCookie.getToken(req) ?: return resp.writer.write(ResponseModel.fail("鉴权失败"))
        val authentication = AuthenticateServer.isCourierORManager(token)
        if (!authentication) return resp.writer.write(ResponseModel.fail("鉴权失败"))
        return resp.writer.write(ResponseModel.success(OrderServer.getMyOrders(token)))
    }

    override fun doGet(req: HttpServletRequest?, resp: HttpServletResponse?) {
        doPost(req, resp)
    }
}
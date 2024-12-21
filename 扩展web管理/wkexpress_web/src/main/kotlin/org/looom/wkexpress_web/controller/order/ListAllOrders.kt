package org.looom.wkexpress_web.controller.order

import jakarta.servlet.annotation.WebServlet
import jakarta.servlet.http.HttpServlet
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.looom.wkexpress_web.server.AuthenticateServer
import org.looom.wkexpress_web.server.OrderServer
import org.looom.wkexpress_web.util.FilterCookie
import org.looom.wkexpress_web.util.ResponseModel

@WebServlet("/order/ListAllOrders")
class ListAllOrders : HttpServlet() {

    override fun doGet(req: HttpServletRequest?, resp: HttpServletResponse?) {
        if (req == null || resp == null) return
        val authentication = AuthenticateServer.isManager(FilterCookie.getToken(req))
        if (!authentication) return resp.writer.write(ResponseModel.fail("鉴权失败"))
        resp.writer.write(ResponseModel.success(OrderServer.getAllOrders()))
    }

    override fun doPost(req: HttpServletRequest?, resp: HttpServletResponse?) {
        doGet(req, resp)
    }
}
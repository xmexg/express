package org.looom.wkexpress_web.controller

import jakarta.servlet.ServletConfig
import jakarta.servlet.annotation.WebServlet
import jakarta.servlet.http.HttpServlet
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse

@WebServlet("/login")
class login : HttpServlet() {

    override fun init(conf: ServletConfig) {
        super.init(conf)
    }

    override fun doPost(req: HttpServletRequest?, resp: HttpServletResponse?) {
        super.doGet(req, resp)
    }

    override fun doGet(req: HttpServletRequest?, resp: HttpServletResponse?) {
        resp?.sendRedirect("login.jsp")
    }
}
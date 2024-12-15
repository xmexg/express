package org.looom.wkexpress_web

import jakarta.servlet.annotation.WebServlet
import jakarta.servlet.http.HttpServlet
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger


@WebServlet(name = "helloServlet", value = ["/helloservlet"])
class HelloServlet : HttpServlet() {
    // 创建 Log4j Logger 实例
    private val logger: Logger = LogManager.getLogger(HelloServlet::class.java)

    private lateinit var message: String

    override fun init() {
        // 初始化消息
        message = "Hello World!"
    }

    override fun doGet(request: HttpServletRequest, response: HttpServletResponse) {
        // 设置响应类型
        response.contentType = "text/html"

        // 输出日志
        logger.info("访问的首页")

        // 响应客户端
        val out = response.writer
        out.println("<html><body>")
        out.println("<h1>$message</h1>")
        out.println("</body></html>")
    }

    override fun destroy() {
        // 销毁操作（如果有需要）
    }
}
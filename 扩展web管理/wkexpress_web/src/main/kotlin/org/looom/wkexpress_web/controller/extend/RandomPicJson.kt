package org.looom.wkexpress_web.controller.extend

import com.alibaba.fastjson2.JSON
import jakarta.servlet.ServletConfig
import jakarta.servlet.ServletException
import jakarta.servlet.annotation.WebServlet
import jakarta.servlet.http.HttpServlet
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import java.io.File
import java.io.IOException
import java.util.*

/**
 * 获取webapp/res/bg目录下的所有文件，并随机选一张生成json返回
 */
@WebServlet("/extend/randomPicJson")
class RandomPicJson : HttpServlet() {

    @Throws(ServletException::class)
    override fun init(config: ServletConfig) {
        // 先初始化,然后才能查看配置文件内容
        super.init(config)
        IMAGE_DIR = config.getInitParameter("imageDir") ?: "/res/bg"
        logger.info("随机图片json已将随机图片资源位置设置为: {}", IMAGE_DIR)
    }

    @Throws(ServletException::class, IOException::class)
    override fun doGet(request: HttpServletRequest, response: HttpServletResponse) {
        // 获取web应用目录
        val realPath = servletContext.getRealPath(IMAGE_DIR)
        val dir = File(realPath)

        // 获取目录下的所有文件
        val validExtensions = setOf(".jpg", ".png", ".webp")
        val files = dir.listFiles { pathname ->
            pathname.isFile && validExtensions.any { pathname.name.endsWith(it, ignoreCase = true) }
        }

        if (files != null && files.isNotEmpty()) {
            // 随机选择一张图片
            val random = Random()
            val randomPic = files[random.nextInt(files.size)]

            // 返回的类型的json格式, {name: file_name, path: filepath(IMAGE_DIR)+file_name}
            // 创建返回的 JSON 格式数据，确保 IMAGE_DIR 不带有尾部 "/"
            val jsonResponse = mapOf(
                "name" to randomPic.name,
                "path" to (IMAGE_DIR?.removeSuffix("/") ?: ".") + "/" + randomPic.name
            )

            // 设置响应类型为 JSON
            response.contentType = "application/json"
            response.characterEncoding = "UTF-8"

            // 使用 fastjson2 序列化 map 为 JSON 字符串
            val json = JSON.toJSONString(jsonResponse)

            // 输出 JSON 响应
            response.writer.write(json)

        } else {
            // 如果没有图片，返回404
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "No images found.")
        }
    }

    override fun doPut(req: HttpServletRequest, resp: HttpServletResponse) {
        super.doPut(req, resp)
    }

    companion object {
        // 配置输出日志
        private val logger: Logger = LogManager.getLogger(RandomPicJson::class.java)

        // 目录路径
        private var IMAGE_DIR: String? = null // 默认位置
    }
}
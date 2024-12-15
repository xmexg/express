package org.looom.wkexpress_web.controller.extend;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.*;
import java.util.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 获取webapp/res/bg目录下的所有文件，并随机选一张返回
 */
@WebServlet("/extend/randomPic")
public class RandomPic extends HttpServlet {

    // 配置输出日志
    private static final Logger logger = LogManager.getLogger(RandomPic.class);
    // 目录路径
    private static String IMAGE_DIR;  // 默认位置

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        String imageDir = getServletContext().getInitParameter("imageDir");
        IMAGE_DIR = imageDir.isEmpty() ? "/res/bg" : imageDir;
        logger.info("随机图片api已将随机图片资源位置设置为: {}", IMAGE_DIR);
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // 获取web应用目录
        String realPath = getServletContext().getRealPath(IMAGE_DIR);
        File dir = new File(realPath);

        // 获取目录下的所有文件
        File[] files = dir.listFiles((File pathname) -> pathname.isFile() && (pathname.getName().endsWith(".jpg") || pathname.getName().endsWith(".png") || pathname.getName().endsWith(".webp")));

        if (files != null && files.length > 0) {
            // 随机选择一张图片
            Random random = new Random();
            File randomPic = files[random.nextInt(files.length)];

            // 设置响应类型为图片
            String contentType = getServletContext().getMimeType(randomPic.getName());
            response.setContentType(contentType != null ? contentType : "image/jpeg");

            // 读取文件并输出到响应流
            try (InputStream in = new FileInputStream(randomPic);
                 OutputStream out = response.getOutputStream()) {
                byte[] buffer = new byte[8192];
                int bytesRead;
                while ((bytesRead = in.read(buffer)) != -1) {
                    out.write(buffer, 0, bytesRead);
                }
            }
        } else {
            // 如果没有图片，返回404
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "No images found.");
        }
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse respons) throws IOException{
        doGet(request, respons);
    }
}

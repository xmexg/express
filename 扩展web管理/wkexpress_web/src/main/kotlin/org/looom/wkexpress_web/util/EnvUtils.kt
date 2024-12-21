package org.looom.wkexpress_web.util

import jakarta.servlet.ServletContextEvent
import jakarta.servlet.ServletContextListener

/**
 * 存放一些环境变量
 */
object EnvUtils : ServletContextListener {
    
    lateinit var ENV: HashMap<Any, Any>

    override fun contextInitialized(sce: ServletContextEvent) {
        super.contextInitialized(sce)
        val servletContext = sce.servletContext
        ENV["web_ui"] = servletContext.getInitParameter("web_ui")
    }

    override fun contextDestroyed(sce: ServletContextEvent?) {
        super.contextDestroyed(sce)
    }
}
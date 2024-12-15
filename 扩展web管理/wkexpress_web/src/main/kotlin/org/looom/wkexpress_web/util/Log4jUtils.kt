package org.looom.wkexpress_web.util

import jakarta.servlet.ServletContextEvent
import jakarta.servlet.ServletContextListener
import org.apache.logging.log4j.LogManager

class Log4jUtils : ServletContextListener {
    override fun contextInitialized(sce: ServletContextEvent?) {
        super.contextInitialized(sce)
        println("log4j 已初始化")
    }

    override fun contextDestroyed(sce: ServletContextEvent?) {
        super.contextDestroyed(sce)
        LogManager.shutdown()
        println("log4j 已关闭")
    }
}
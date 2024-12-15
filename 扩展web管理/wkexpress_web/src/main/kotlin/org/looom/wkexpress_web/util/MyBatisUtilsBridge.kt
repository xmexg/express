package org.looom.wkexpress_web.util

import jakarta.servlet.ServletContextEvent
import jakarta.servlet.ServletContextListener

/**
 * 数据库的连接桥
 * 因为java不支持object写法的单例对象, 无法编译运行, 通过该连接桥来适配 Servlet 的生命周期
 */
class MyBatisUtilsBridge : ServletContextListener {
    override fun contextInitialized(sce: ServletContextEvent) {
        MyBatisUtils.contextInitialized(sce)
    }

    override fun contextDestroyed(sce: ServletContextEvent?) {
        MyBatisUtils.contextDestroyed(sce)
    }
}
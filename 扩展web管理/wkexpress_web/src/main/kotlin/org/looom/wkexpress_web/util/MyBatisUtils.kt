package org.looom.wkexpress_web.util

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import jakarta.servlet.ServletContextEvent
import jakarta.servlet.ServletContextListener
import org.apache.ibatis.mapping.Environment
import org.apache.ibatis.session.Configuration
import org.apache.ibatis.session.SqlSession
import org.apache.ibatis.session.SqlSessionFactory
import org.apache.ibatis.session.SqlSessionFactoryBuilder
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory

/**
 * mybatis 连接类
 * object单一对象,全局只new一个
 */

/**
 * java不支持如下单例写法, 创建一个连接桥来适配 Servlet 的生命周期
 */
object MyBatisUtils : ServletContextListener {

    // 延时初始化mysql会话
    private lateinit var sqlSessionFactory: SqlSessionFactory

    override fun contextInitialized(sce: ServletContextEvent) {
        super.contextInitialized(sce)
        val servletContext = sce.servletContext

        // 配置Hikari数据源
        val hikariConfig = HikariConfig().apply{
            jdbcUrl = servletContext.getInitParameter("spring.datasource.url")
            username = servletContext.getInitParameter("spring.datasource.username")
            password = servletContext.getInitParameter("spring.datasource.password")
            driverClassName = servletContext.getInitParameter("spring.datasource.driverClassName")
            maximumPoolSize = 10
            minimumIdle = 5
        }
        val dataSource = HikariDataSource(hikariConfig)

        // 配置mybatis
        val mybatisEnvironment = Environment(
            "development",  // 环境名, 可随意取名
            JdbcTransactionFactory(),  // 使用JDBC事务管理
            dataSource  // Hikari数据源
        )
        val myBatisConfig = Configuration(mybatisEnvironment).apply { addMappers("org.looom.wkexpress_web.mapper") }
        sqlSessionFactory = SqlSessionFactoryBuilder().build(myBatisConfig)
    }

    override fun contextDestroyed(sce: ServletContextEvent?) {
        super.contextDestroyed(sce)
        // 关闭数据池
        (sqlSessionFactory.configuration.environment.dataSource as? HikariDataSource)?.close()
        println("数据库连接池已关闭")
    }

    fun getSession(): SqlSession = sqlSessionFactory.openSession(true)  // 自动提交
}
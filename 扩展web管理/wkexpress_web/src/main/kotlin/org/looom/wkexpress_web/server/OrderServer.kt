package org.looom.wkexpress_web.server

import org.looom.wkexpress_web.entity.Ordering
import org.looom.wkexpress_web.mapper.OrderMapper
import org.looom.wkexpress_web.util.MyBatisUtils

class OrderServer {
    companion object{
        val order_mapper = MyBatisUtils.getSession().getMapper(OrderMapper::class.java)

        fun getAllOrders(): List<Ordering>? = order_mapper.getAllOrders()
        fun getMyOrders(token: String): List<Ordering>? = order_mapper.getMyOrders(token)
    }
}
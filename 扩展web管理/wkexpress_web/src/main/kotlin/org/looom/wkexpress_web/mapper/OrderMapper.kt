package org.looom.wkexpress_web.mapper

import org.apache.ibatis.annotations.Select
import org.looom.wkexpress_web.entity.Ordering

interface OrderMapper {

    @Select("SELECT * FROM booking")
    fun getAllOrders(): List<Ordering>?

    @Select("SELECT b.* FROM booking b INNER JOIN userlist u ON u.user_openid = b.shipper WHERE u.user_token = #{token}")
    fun getMyOrders(token: String): List<Ordering>?
}
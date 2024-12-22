package org.looom.wkexpress_web.server

import org.looom.wkexpress_web.mapper.UserMapper
import org.looom.wkexpress_web.util.MyBatisUtils

/**
 * 鉴权
 */
class AuthenticateServer {
    companion object {
        private const val COURIER = 1
        private const val USER = 2
        private const val MANAGER = 9
        private val user_mapper: UserMapper = MyBatisUtils.getSession().getMapper(UserMapper::class.java)

        fun isCourier(token: String?): Boolean = token!=null && user_mapper.getUser(token)?.user_type== COURIER
        fun isUSER(token: String?): Boolean = token!=null && user_mapper.getUser(token)?.user_type== USER
        fun isManager(token: String?): Boolean = token!=null && user_mapper.getUser(token)?.user_type== MANAGER
        fun isCourierORManager(token: String?): Boolean = isCourier(token) || isManager(token)

        fun isCourier(user_type: Int?): Boolean = user_type== COURIER
        fun isUSER(user_type: Int?): Boolean = user_type== USER
        fun isManager(user_type: Int?): Boolean = user_type== MANAGER
        fun isCourierORManager(user_type: Int?): Boolean = isCourier(user_type) || isManager(user_type)
    }
}
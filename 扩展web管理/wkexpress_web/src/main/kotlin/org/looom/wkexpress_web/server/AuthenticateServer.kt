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
        fun isMANAGER(token: String?): Boolean = token!=null && user_mapper.getUser(token)?.user_type== MANAGER
    }
}
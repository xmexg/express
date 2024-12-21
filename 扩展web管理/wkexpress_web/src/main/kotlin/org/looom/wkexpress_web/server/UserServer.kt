package org.looom.wkexpress_web.server

import org.looom.wkexpress_web.entity.User
import org.looom.wkexpress_web.mapper.UserMapper
import org.looom.wkexpress_web.util.MyBatisUtils

/**
 * 连接controller层与mapper层之间
 *
 * user篇
 */
class UserServer {
    companion object{
        val user_mapper = MyBatisUtils.getSession().getMapper(UserMapper::class.java)

        fun getAllUser(): List<User>? = user_mapper.getAllUsers()

        fun getUser(token: String?): User? = token?.let { user_mapper.getUser(it) }
    }
}
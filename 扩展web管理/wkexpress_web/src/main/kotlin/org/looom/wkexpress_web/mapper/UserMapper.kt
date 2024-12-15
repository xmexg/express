package org.looom.wkexpress_web.mapper

import org.apache.ibatis.annotations.Select
import org.looom.wkexpress_web.entity.User

interface UserMapper {

    // 获取全部用户
    @Select("SELECT * FROM userlist")
    fun getAllUsers(): List<User>?

    // 通过token获取指定用户
    @Select("SELECT * FROM userlist WHERE user_token = #{token}")
    fun getUser(token: String): User?
}
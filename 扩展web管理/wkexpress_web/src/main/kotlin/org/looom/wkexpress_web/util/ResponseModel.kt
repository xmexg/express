package org.looom.wkexpress_web.util

import com.alibaba.fastjson2.JSON

/**
 * 封装返回结果
 */
class ResponseModel {

    // 创建伴生对象
    companion object {

        var code = 0
        var message: String? = null
        var data: Any? = null

        @JvmStatic  // 使static
        fun make(code: Int, message: String?, data: Any?): String {
            this.code = code
            this.message = message
            this.data = data ?: ""
            return JSON.toJSONString(this)
        }

        @JvmStatic
        fun success(): String {
            return make(SUCCESS, "success", null)
        }

        @JvmStatic
        fun success(data: Any?): String {
            return make(SUCCESS, "success", data)
        }

        @JvmStatic
        fun fail(): String {
            return make(FAIL, "fail", null)
        }

        @JvmStatic
        fun fail(message: String?): String {
            return make(FAIL, message, null)
        }

        @JvmStatic
        fun unauthorized(): String {
            return make(UNAUTHORIZED, "用户未认证", null)
        }

        @JvmStatic
        fun unauthorized(message: String?): String {
            return make(UNAUTHORIZED, message, null)
        }

        @JvmStatic
        fun invaliduser(): String {
            return make(INVALIDUSER, "无效用户", null)
        }

        @JvmStatic
        fun invaliduser(message: String?): String {
            return make(INVALIDUSER, message, null)
        }

        @JvmStatic
        fun notfound(): String {
            return make(NOTFOUND, "未找到", null)
        }

        @JvmStatic
        fun notfound(message: String?): String {
            return make(NOTFOUND, message, null)
        }

        @JvmStatic
        fun notlogin(): String {
            return make(NOTLOGIN, "用户未登录", null)
        }

        @JvmStatic
        fun notlogin(message: String?): String {
            return make(NOTLOGIN, message, null)
        }

        override fun toString(): String {
            return JSON.toJSONString(this)
        }

        const val SUCCESS: Int = 200
        const val FAIL: Int = 400
        const val UNAUTHORIZED: Int = 401
        const val INVALIDUSER: Int = 403
        const val NOTFOUND: Int = 404
        const val NOTLOGIN: Int = 405
    }
}
package org.looom.wkexpress_web.util

import jakarta.servlet.http.HttpServletRequest

/**
 * 从请求头里拿指定cooken
 */
class FilterCookie {
    companion object {
        fun getToken(cookie: String): String? = get("token", cookie)

        fun getToken(req: HttpServletRequest): String? = get("token", req)

        fun getJSESSIONID(cookie: String): String? = get("JSESSIONID", cookie)

        fun getJSESSIONID(req: HttpServletRequest): String? = get("JSESSIONID", req)

        fun get(string: String, req: HttpServletRequest): String? {
            val cookie = req.cookies;
            if (cookie != null) {
                for (c in cookie) {
                    if (c.name == string) {
                        return c.value
                    }
                }
            }
            return null
        }

        fun get(string: String, cookie: String): String? {
            val cookieList = cookie.split(";")
            for (c in cookieList) {
                if (c.contains(string)) {
                    return c.split("=")[1]
                }
            }
            return null
        }
    }
}
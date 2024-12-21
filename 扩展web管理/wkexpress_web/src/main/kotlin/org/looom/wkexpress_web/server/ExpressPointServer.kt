package org.looom.wkexpress_web.server

import org.looom.wkexpress_web.mapper.ExpressPoint
import org.looom.wkexpress_web.util.MyBatisUtils

class ExpressPointServer {
    companion object{
        val expressPoint = MyBatisUtils.getSession().getMapper(ExpressPoint::class.java)

        fun getAllPoints(): List<String>? = expressPoint.getAllPoints()

        fun deleteAllPoints(): Boolean = expressPoint.deleteAllPoints()

        fun insertPoint(point: String): Boolean = expressPoint.insertPoint(point)

        fun resetPoint(list: List<String>?): Boolean {
            var status: Boolean
            status = deleteAllPoints()
            list?.forEach{ point -> status = insertPoint(point) }
            return status
        }
    }
}
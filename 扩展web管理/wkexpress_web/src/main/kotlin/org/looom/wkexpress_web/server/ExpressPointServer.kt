package org.looom.wkexpress_web.server

import org.looom.wkexpress_web.mapper.ExpressPointMapper
import org.looom.wkexpress_web.util.MyBatisUtils

class ExpressPointServer {
    companion object{
        val expressPointMapper = MyBatisUtils.getSession().getMapper(ExpressPointMapper::class.java)

        fun getAllPoints(): List<String>? = expressPointMapper.getAllPoints()

        fun deleteAllPoints(): Boolean = expressPointMapper.deleteAllPoints()

        fun insertPoint(point: String): Boolean = expressPointMapper.insertPoint(point)

        fun resetPoint(list: List<String>?): Boolean {
            var status: Boolean
            status = deleteAllPoints()
            list?.forEach{ point -> status = insertPoint(point) }
            return status
        }
    }
}
package org.looom.wkexpress_web.mapper

import org.apache.ibatis.annotations.Delete
import org.apache.ibatis.annotations.Insert
import org.apache.ibatis.annotations.Select

interface ExpressPoint {

    @Select("SELECT * FROM expresspoint")
    fun getAllPoints(): List<String>?

    @Delete("DELETE FROM expresspoint")
    fun deleteAllPoints(): Boolean

    @Insert("INSERT INTO expresspoint (point) VALUES (#{point})")
    fun insertPoint(point: String): Boolean

}


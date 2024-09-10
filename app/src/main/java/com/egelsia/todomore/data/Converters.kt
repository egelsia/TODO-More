package com.egelsia.todomore.data

import androidx.room.TypeConverter
import java.util.Date

class Converters {
    @TypeConverter
    fun fromTimeStamp(value: Long?) : Date? {
        return value?.let {Date(it)}
    }

    @TypeConverter
    fun dateToTimeStamp(date: Date?): Long? {
        return date?.time
    }

//
//    @TypeConverter
//    fun fromColor(color: Color): Long {
//        return color.value.toLong()
//    }
//
//    @TypeConverter
//    fun toColor(value: Long): Color {
//        return Color(value.toULong())
//    }
}
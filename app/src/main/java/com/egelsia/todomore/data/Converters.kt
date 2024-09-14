package com.egelsia.todomore.data

import androidx.room.TypeConverter
import java.time.LocalDate

class Converters {
    @TypeConverter
    fun fromTimeStamp(value: String?) : LocalDate? {
        return value?.let {LocalDate.parse(it)}
    }

    @TypeConverter
    fun dateToTimeStamp(date: LocalDate?): String? {
        return date?.toString()
    }

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
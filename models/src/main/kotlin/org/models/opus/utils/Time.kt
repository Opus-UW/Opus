package org.models.opus.utils

import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

fun String.toLDT(): LocalDateTime{
    return try{
        var a = Instant.parse(this).toLocalDateTime(TimeZone.UTC).toString()
        if(a.count{it == ':'} == 1){
            a = "${a}:01"
        }
        LocalDateTime.parse(a)
    } catch (e: Exception){
        var a = LocalDateTime.parse(this).toString()
        if(a.count{it == ':'} == 1){
           a = "${a}:01"
        }
        println(LocalDateTime.parse(a))
        LocalDateTime.parse(a)
    }
}
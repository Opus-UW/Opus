package org.models.opus.utils

import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

fun String.toLDT(): LocalDateTime{
    lateinit var a: String
    try{
        a = Instant.parse(this).toLocalDateTime(TimeZone.UTC).toString()
    } catch (e: Exception){
        a = LocalDateTime.parse(this).toString()
    } finally {
        if(a.count{it == ':'} == 1){
            a = "${a}:01"
        }
    }
    return LocalDateTime.parse(a)
}
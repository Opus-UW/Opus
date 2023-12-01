package org.models.opus.utils

import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

fun String.toLDT(): LocalDateTime{
    return try{
        Instant.parse(this).toLocalDateTime(TimeZone.UTC)
    } catch (e: Exception){
        LocalDateTime.parse(this)
    }
}
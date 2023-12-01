package org.models.opus.utils

import com.google.api.client.util.DateTime
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

fun String.toLDT(): LocalDateTime{
    return try{
        Instant.parse(this).toLocalDateTime(TimeZone.UTC)
    } catch (e: Exception){
        Instant.parse(DateTime(this).toStringRfc3339()).toLocalDateTime(TimeZone.UTC)
    }
}
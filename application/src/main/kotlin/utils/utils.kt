package utils

import kotlinx.datetime.DatePeriod
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.minus
import kotlinx.datetime.plus

operator fun LocalDateTime.minus(value: Int): LocalDateTime = LocalDateTime(this.date - DatePeriod(0,0, value), this.time )
operator fun LocalDateTime.plus(value: Int): LocalDateTime = LocalDateTime(this.date + DatePeriod(0,0, value), this.time )
fun LocalDateTime.plusMonth(value: Int): LocalDateTime = LocalDateTime(this.date + DatePeriod(0, value, 0), this.time )
fun LocalDateTime.minusMonth(value: Int): LocalDateTime = LocalDateTime(this.date - DatePeriod(0,value, 0), this.time )
package utils

import androidx.compose.ui.graphics.Color
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import org.models.opus.models.Colour

operator fun LocalDateTime.minus(value: Int): LocalDateTime = LocalDateTime(this.date - DatePeriod(0,0, value), this.time )
operator fun LocalDateTime.plus(value: Int): LocalDateTime = LocalDateTime(this.date + DatePeriod(0,0, value), this.time )
fun LocalDateTime.plusMonth(value: Int): LocalDateTime = LocalDateTime(this.date + DatePeriod(0, value, 0), this.time )
fun LocalDateTime.minusMonth(value: Int): LocalDateTime = LocalDateTime(this.date - DatePeriod(0,value, 0), this.time )

fun Colour.toColor() : Color {
    return Color(this.red, this.green, this.blue, 255)
}

fun Color.toColour(): Colour {
    return Colour((this.red*255).toInt(), (this.green*255).toInt(), (this.blue*255).toInt())
}
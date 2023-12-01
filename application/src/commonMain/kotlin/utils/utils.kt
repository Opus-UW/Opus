package utils

import androidx.compose.ui.graphics.Color
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import org.models.opus.models.Colour
import org.models.opus.models.toEnum
import ui.theme.md_theme_dark_tags
import ui.theme.md_theme_light_tags

operator fun LocalDateTime.minus(value: Int): LocalDateTime = LocalDateTime(this.date - DatePeriod(0,0, value), this.time )
operator fun LocalDateTime.plus(value: Int): LocalDateTime = LocalDateTime(this.date + DatePeriod(0,0, value), this.time )
fun LocalDateTime.plusMonth(value: Int): LocalDateTime = LocalDateTime(this.date + DatePeriod(0, value, 0), this.time )
fun LocalDateTime.minusMonth(value: Int): LocalDateTime = LocalDateTime(this.date - DatePeriod(0,value, 0), this.time )



fun Color.toColour(): Colour {
    md_theme_light_tags.forEachIndexed{i, color ->
        if (color == this){
            val colour = i.toEnum<Colour>()
            println (colour)
            return colour ?: Colour.CORAL
        }
    }
    md_theme_dark_tags.forEachIndexed{i, color ->
        if (color == this){
            val colour = i.toEnum<Colour>()
            println (colour)
            return colour ?: Colour.CORAL
        }
    }
    return Colour.CORAL
}
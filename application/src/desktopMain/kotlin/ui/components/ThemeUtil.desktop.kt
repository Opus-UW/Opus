package ui.components

import androidx.compose.runtime.Composable
import ui.theme.isDarkTheme
import java.io.File

private val SETTINGS_DIRECTORY_PATH = "${System.getProperty("user.home")}/.opus/settings"

actual fun storeTheme(
    boolean: Boolean
){
    val settingsFile = File(SETTINGS_DIRECTORY_PATH)
    settingsFile.writeText("theme: " + if (boolean) "1" else "0")
}

actual fun getTheme(sysTheme: Boolean) :  Boolean {
    val settingsFile = File(SETTINGS_DIRECTORY_PATH)
    var isDark = true

    if (settingsFile.exists()){
        settingsFile.forEachLine{
            if (it.substring(0, 5) == "theme"){
                val isDarkSetting = it.substring(it.length-1)
                if (isDarkSetting == "1"){
                    isDark = true
                }
                else if (isDarkSetting == "0"){
                    isDark = false
                }
            }
        }
    }
    else {
        settingsFile.writeText("theme: " + if (sysTheme) "1" else "0")
        isDark = sysTheme
    }
    return isDark
}
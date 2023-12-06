package ui.components

import androidx.compose.runtime.Composable
import ui.theme.isDarkTheme
import java.io.File

private val OPUS_DIRECTORY_PATH = "${System.getProperty("user.home")}/.opus"
private val SETTINGS_DIRECTORY_PATH = "$OPUS_DIRECTORY_PATH/settings"

actual fun storeTheme(
    boolean: Boolean
){
    val opusDirectory = File(OPUS_DIRECTORY_PATH)
    val settingsFile = File(SETTINGS_DIRECTORY_PATH)
    if(!opusDirectory.exists()){
        opusDirectory.mkdir()
    }
    if (!settingsFile.exists()){
        settingsFile.createNewFile()
    }
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
        val opusDirectory = File(OPUS_DIRECTORY_PATH)
        if(!opusDirectory.exists()){
            opusDirectory.mkdir()
        }
        settingsFile.createNewFile()
        settingsFile.writeText("theme: " + if (sysTheme) "1" else "0")
        isDark = sysTheme
    }
    return isDark
}
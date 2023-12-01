package ui.components

import androidx.compose.runtime.Composable
import ui.theme.isDarkTheme
import java.io.File

private val SETTINGS_DIRECTORY_PATH = "${System.getProperty("user.home")}/.opus/settings"

actual fun storeTheme(
    boolean: Boolean
){
    val settingsFile = File(SETTINGS_DIRECTORY_PATH)
    settingsFile.writeText("theme: $boolean")
}

actual fun getTheme(sysTheme: Boolean) :  Boolean {
    val settingsFile = File(SETTINGS_DIRECTORY_PATH)
    var isDark = true

    if (settingsFile.exists()){
        print ("Settings exists")
        settingsFile.forEachLine{
            if (it.substring(0, 5) == "theme"){
                val isDarkSetting = it.substring(it.length-2)
                print (isDarkSetting)
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
        println ("Settings does not exist")
        println (SETTINGS_DIRECTORY_PATH)
        settingsFile.writeText("theme: ${sysTheme}")
        println("Wrote file? ${settingsFile.isFile}")
        isDark = sysTheme
    }
    return isDark
}
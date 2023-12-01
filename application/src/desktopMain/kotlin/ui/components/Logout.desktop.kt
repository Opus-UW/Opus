package ui.components

import moe.tlaster.precompose.navigation.Navigator
import viewmodels.MainViewModel
import java.io.File

actual fun Logout(
    viewModel: MainViewModel,
    navigator: Navigator
){
    val TOKENS_DIRECTORY_PATH = "${System.getProperty("user.home")}/.opus/tokens"
    val tokenFile = File(TOKENS_DIRECTORY_PATH)
    tokenFile.deleteRecursively()
    navigator.navigate("/login")
    viewModel.setCurrentScreen("/login")
}
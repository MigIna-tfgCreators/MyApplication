package com.example.myapplication.classes.managers

import android.content.Intent
import android.os.Bundle
import androidx.annotation.IdRes
import androidx.navigation.NavController
import com.example.myapplication.classes.providers.ContextProviderInterface

class NavigationManager(private val contextProvider: ContextProviderInterface):
    NavigationManagerInterface {

    val navController: NavController?
        get() = contextProvider.currentActivity?.navHostFragment?.navController

    override fun navigate(navigate: Navigation) {
        with(navigate) {
            when(this) {
                is Navigation.to ->
                    navController?.navigate(destination)
                is Navigation.main -> {
                    val context = contextProvider.currentActivity ?: return
                    val intent = Intent(context, destination)
                    if(bundle != null) intent.putExtras(bundle)
                    context.startActivity(intent)
                    //context.overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
                    context.finish()
                }
                is Navigation.popBackStack ->
                    navController?.popBackStack()
                is Navigation.withArguments ->
                    navController?.navigate(destination, arguments)
            }
        }
    }

    override fun dissmis() {
        val context = contextProvider.currentActivity ?: return
        context.finish()
    }
}
sealed class Navigation {
    data class to(@IdRes val destination: Int): Navigation()
    data class withArguments(@IdRes val destination: Int, val arguments: Bundle?): Navigation()
    object popBackStack: Navigation()
    data class main(val destination: Class<*>, val bundle: Bundle? = null): Navigation()

}
package com.example.myapplication.classes.modules.main.cartelera.routing

import android.os.Bundle
import android.util.Log
import com.example.myapplication.R
import com.example.myapplication.classes.managers.Navigation
import com.example.myapplication.classes.managers.NavigationManagerInterface

class CarteleraRouting(
    private val navigationManager: NavigationManagerInterface
): CarteleraRoutingInterface {

    override fun goToDetails(bundle: Bundle?) {
        navigationManager.navigate(Navigation.withArguments(R.id.detallesFragment, bundle))
    }
}
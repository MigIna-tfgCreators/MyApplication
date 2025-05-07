package com.example.myapplication.classes.modules.main.routing



import android.os.Bundle
import android.util.Log
import com.example.myapplication.R
import com.example.myapplication.classes.managers.Navigation
import com.example.myapplication.classes.managers.NavigationManagerInterface

class MainRouting(
    private val navigationManager: NavigationManagerInterface
): MainRoutingInterface {
    override fun navigateToCartelera() {
        Log.i("Navegacion Profunda", "Cartelera")
        navigationManager.navigate(Navigation.to(R.id.carteleraFragment))
    }

    override fun navigateToTop() {
        Log.i("Navegacion Profunda", "Top")
        navigationManager.navigate(Navigation.to(R.id.topFragment))
    }

    override fun navigateToDetalles(bundle: Bundle?) {
        Log.i("Navegacion Profunda", "Detalle")
        navigationManager.navigate(Navigation.withArguments(R.id.detallesFragment, bundle))
    }

    override fun navigateToBusqueda() {
        Log.i("Navegacion Profunda", "Busqueda")
        navigationManager.navigate(Navigation.to(R.id.busquedaFragment))
    }

    override fun navigateToFavoritos() {
        Log.i("Navegacion Profunda", "Lista")
        navigationManager.navigate(Navigation.to(R.id.favoritosFragment))
    }

}
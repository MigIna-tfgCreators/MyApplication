package com.example.myapplication.configurations

import com.example.myapplication.classes.managers.NavigationManager
import com.example.myapplication.classes.managers.NavigationManagerInterface
import com.example.myapplication.classes.models.commonErrors.showErrs.DialogErrs
import com.example.myapplication.classes.models.commonErrors.showErrs.DialogErrsInterface
import com.example.myapplication.classes.modules.auth.routing.RegisterRouting
import com.example.myapplication.classes.modules.auth.routing.RegisterRoutingInterface
import com.example.myapplication.classes.modules.auth.viewModel.SignViewModel
import com.example.myapplication.classes.modules.main.activity.routing.MainRouting
import com.example.myapplication.classes.modules.main.activity.routing.MainRoutingInterface
import com.example.myapplication.classes.modules.main.activity.viewmodel.PeliculasViewModel
import com.example.myapplication.classes.modules.main.cartelera.routing.CarteleraRouting
import com.example.myapplication.classes.modules.main.cartelera.routing.CarteleraRoutingInterface
import com.example.myapplication.classes.modules.main.cartelera.viewmodel.CarteleraViewModel
import com.example.myapplication.classes.modules.main.detalles.viewmodel.DetallesViewModel
import com.example.myapplication.classes.providers.ContextProvider
import com.example.myapplication.classes.providers.ContextProviderInterface
import com.example.myapplication.classes.repositories.PeliculasRepository.PeliculasRepository
import com.example.myapplication.classes.repositories.PeliculasRepository.PeliculasRepositoryImpl
import com.example.myapplication.classes.repositories.authUserRepository.AuthRepository
import com.example.myapplication.classes.repositories.authUserRepository.AuthRepositoyImpl
import com.example.myapplication.classes.services.authUserService.AuthService
import com.example.myapplication.classes.services.authUserService.AuthServiceImpl
import com.example.myapplication.classes.services.network.API
import com.example.myapplication.classes.services.network.PeliculaService
import com.example.myapplication.classes.services.network.WebService
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val appModule = module{
    single<ContextProvider>{ ContextProvider() }
    single<ContextProviderInterface> {get<ContextProvider>()}
    single{Retrofit.Builder()
        .baseUrl(API.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    }
    single{ get<Retrofit>().create(PeliculaService::class.java)}
    single{ get<Retrofit>().create(WebService::class.java)}
    factory<AuthService> { AuthServiceImpl() }

    factory<NavigationManagerInterface> { NavigationManager(get<ContextProviderInterface>()) }

    factory<MainRoutingInterface>{ MainRouting(get()) }
    factory<RegisterRoutingInterface>{ RegisterRouting(get()) }
    factory<CarteleraRoutingInterface> { CarteleraRouting(get()) }
    factory<DialogErrsInterface>{ DialogErrs(get())}

    factory<AuthRepository> { AuthRepositoyImpl(get()) }
    factory<PeliculasRepository> { PeliculasRepositoryImpl(get(),get()) }

    viewModel{ SignViewModel(get(), get(), get(), get()) }
    viewModel{ PeliculasViewModel(get(), get()) }
    viewModel{ CarteleraViewModel(get(), get()) }
    viewModel{ DetallesViewModel(get()) }
}
package com.example.myapplication.configurations

import com.example.myapplication.classes.managers.NavigationManagerInterface
import com.example.myapplication.classes.modules.login.viewModel.SignViewModel
import com.example.myapplication.classes.providers.ContextProvider
import com.example.myapplication.classes.providers.ContextProviderInterface
import com.example.myapplication.classes.repositories.authUserRepository.AuthRepository
import com.example.myapplication.classes.repositories.authUserRepository.AuthRepositoyImpl
import com.example.myapplication.classes.services.authUserService.AuthService
import com.prueba.apphouse.classes.managers.NavigationManager
import com.prueba.apphouse.classes.models.commonErrors.showErrs.DialogErrs
import com.prueba.apphouse.classes.models.commonErrors.showErrs.DialogErrsInterface
import com.prueba.apphouse.classes.modules.start.routing.RegisterRouting
import com.prueba.apphouse.classes.modules.start.routing.RegisterRoutingInterface
import com.prueba.apphouse.classes.services.firebase.userService.AuthServiceImpl
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module{
    single<ContextProvider>{ ContextProvider() }
    single<ContextProviderInterface> {get<ContextProvider>()}
    factory<NavigationManagerInterface> { NavigationManager(get<ContextProviderInterface>()) }

    factory<AuthService> { AuthServiceImpl() }

    factory<RegisterRoutingInterface>{ RegisterRouting(get()) }
    factory<DialogErrsInterface>{ DialogErrs(get())}

    factory<AuthRepository> { AuthRepositoyImpl(get()) }

    viewModel{ SignViewModel(get(), get(), get(), get()) }
}
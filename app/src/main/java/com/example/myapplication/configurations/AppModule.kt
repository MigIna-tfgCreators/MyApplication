package com.example.myapplication.configurations

import com.example.myapplication.BuildConfig
import com.example.myapplication.classes.managers.NavigationManager
import com.example.myapplication.classes.managers.NavigationManagerInterface
import com.example.myapplication.classes.modules.auth.activity.routing.RegisterRouting
import com.example.myapplication.classes.modules.auth.activity.routing.RegisterRoutingInterface
import com.example.myapplication.classes.modules.auth.activity.viewModel.SignViewModel
import com.example.myapplication.classes.modules.main.activity.routing.MainRouting
import com.example.myapplication.classes.modules.main.activity.routing.MainRoutingInterface
import com.example.myapplication.classes.modules.main.activity.viewmodel.MoviesMainViewModel
import com.example.myapplication.classes.modules.main.search.viewmodel.SearchViewModel
import com.example.myapplication.classes.modules.main.now_playing.viewmodel.NowPlayingViewModel
import com.example.myapplication.classes.modules.main.details.viewmodel.DetailsViewModel
import com.example.myapplication.classes.modules.main.personal.viewmodel.PersonalListViewModel
import com.example.myapplication.classes.providers.ContextProvider
import com.example.myapplication.classes.providers.ContextProviderInterface
import com.example.myapplication.classes.repositories.api.moviesRepository.MoviesRepository
import com.example.myapplication.classes.repositories.api.moviesRepository.MoviesRepositoryImpl
import com.example.myapplication.classes.repositories.firebase.authUserRepository.AuthRepository
import com.example.myapplication.classes.repositories.firebase.authUserRepository.AuthRepositoyImpl
import com.example.myapplication.classes.repositories.firebase.usermovieRepository.UserMovieRepository
import com.example.myapplication.classes.repositories.firebase.usermovieRepository.UserMovieRepositoryImpl
import com.example.myapplication.classes.services.firebase.authUserService.AuthService
import com.example.myapplication.classes.services.firebase.authUserService.AuthServiceImpl
import com.example.myapplication.classes.services.api.APIServiceInterface
import com.example.myapplication.classes.services.api.movies.MovieService
import com.example.myapplication.classes.services.api.movies.MovieServiceImpl
import com.example.myapplication.classes.services.firebase.movieService.MovieUserService
import com.example.myapplication.classes.services.firebase.movieService.MovieUserServiceImpl
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val appModule = module{
    single<ContextProvider>{ ContextProvider() }
    single<ContextProviderInterface> {get<ContextProvider>()}
    single{Retrofit.Builder()
        .baseUrl(BuildConfig.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    }
    single{ get<Retrofit>().create(APIServiceInterface::class.java)}
    factory<AuthService> { AuthServiceImpl() }
    factory<MovieUserService> { MovieUserServiceImpl() }
    factory<MovieService> { MovieServiceImpl(get()) }

    factory<NavigationManagerInterface> { NavigationManager(get<ContextProviderInterface>()) }

    factory<MainRoutingInterface>{ MainRouting(get()) }
    factory<RegisterRoutingInterface>{ RegisterRouting(get()) }

    factory<AuthRepository> { AuthRepositoyImpl(get()) }
    factory<MoviesRepository> { MoviesRepositoryImpl(get()) }
    factory<UserMovieRepository> { UserMovieRepositoryImpl(get()) }

    viewModel{ SignViewModel(get(), get(), get()) }
    viewModel{ MoviesMainViewModel(get()) }
    viewModel{ NowPlayingViewModel(get(), get()) }
    viewModel{ DetailsViewModel(get(), get()) }
    viewModel{ SearchViewModel(get(), get(), get()) }
    viewModel{ PersonalListViewModel(get(), get()) }
}
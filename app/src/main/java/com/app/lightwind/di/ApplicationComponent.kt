package com.app.lightwind.di


import com.app.lightwind.AndroidApplication
import com.app.lightwind.di.viewmodel.ViewModelModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [ApplicationModule::class, ViewModelModule::class])
interface ApplicationComponent {
    fun inject(application: AndroidApplication)
//    fun inject(routeActivity: RouteActivity)
//
//    fun inject(moviesFragment: MoviesFragment)
//    fun inject(movieDetailsFragment: MovieDetailsFragment)
}

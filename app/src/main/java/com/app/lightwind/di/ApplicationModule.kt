package com.app.lightwind.di

import android.content.Context
import com.app.lightwind.AndroidApplication
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton
import com.app.lightwind.BuildConfig

@Module
class ApplicationModule(private val application: AndroidApplication) {

    @Provides @Singleton fun provideApplicationContext(): Context = application

    @Provides @Singleton fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
                .baseUrl("https://raw.githubusercontent.com/android10/Sample-Data/master/Android-CleanArchitecture-Kotlin/")
                .client(createClient())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
    }

    private fun createClient(): OkHttpClient {
        val okHttpClientBuilder: OkHttpClient.Builder = OkHttpClient.Builder()
        if (BuildConfig.DEBUG) {
            val loggingInterceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC)
            okHttpClientBuilder.addInterceptor(loggingInterceptor)
        }
        return okHttpClientBuilder.build()
    }

   // @Provides @Singleton fun provideMoviesRepository(dataSource: MoviesRepository.Network): MoviesRepository = dataSource
}

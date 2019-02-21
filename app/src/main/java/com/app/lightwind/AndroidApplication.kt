package com.app.lightwind

import android.app.Application
import com.app.lightwind.di.ApplicationComponent
import com.app.lightwind.di.ApplicationModule
import com.app.lightwind.di.DaggerApplicationComponent
import com.facebook.stetho.Stetho
import com.squareup.leakcanary.LeakCanary

class AndroidApplication : Application() {
    val components by lazy { Components(this) }
    val appComponent: ApplicationComponent by lazy(mode = LazyThreadSafetyMode.NONE) {
        DaggerApplicationComponent
                .builder()
                .applicationModule(ApplicationModule(this))
                .build()
    }

    override fun onCreate() {
        super.onCreate()
        this.injectMembers()
        //memory leak detect
        this.initializeLeakDetection()
        //observe with stetho
        this.initializeStetho()
    }

    private fun injectMembers() = appComponent.inject(this)
    private fun initializeStetho(){
        if(BuildConfig.DEBUG){
            Stetho.initializeWithDefaults(this)
        }
    }
    private fun initializeLeakDetection() {
        if (BuildConfig.DEBUG) {
            if (LeakCanary.isInAnalyzerProcess(this)) {
                // This process is dedicated to LeakCanary for heap analysis.
                // You should not init your app in this process.
                return
            }
            LeakCanary.install(this)
        }
    }
}

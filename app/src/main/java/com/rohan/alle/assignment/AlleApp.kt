package com.rohan.alle.assignment

import android.app.Application
import com.rohan.alle.assignment.di.repositoryModule
import com.rohan.alle.assignment.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

/***
 * Created by Rohan
 * [AlleApp] : Application class for Alle Assignment app
 */
class AlleApp : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger(Level.ERROR)
            androidContext(this@AlleApp)
            modules(
                listOf(repositoryModule, viewModelModule)
            )
        }
    }
}
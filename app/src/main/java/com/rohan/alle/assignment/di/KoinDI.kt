package com.rohan.alle.assignment.di

import com.rohan.alle.assignment.data.repository.ScreenshotWorkerRepository
import com.rohan.alle.assignment.data.repositoryImpl.ScreenshotWorkerRepositoryImpl
import com.rohan.alle.assignment.ui.gallery.GalleryViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

/***
 * Created by Rohan
 * Koin Dependency Injection modules
 */

val repositoryModule = module {
    single<ScreenshotWorkerRepository> {
        ScreenshotWorkerRepositoryImpl()
    }
}

val viewModelModule = module {
    viewModel {
        GalleryViewModel(get())
    }
}
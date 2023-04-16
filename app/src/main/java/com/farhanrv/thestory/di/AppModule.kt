package com.farhanrv.thestory.di

import androidx.room.Room
import com.farhanrv.thestory.data.AppRepository
import com.farhanrv.thestory.data.local.UserPreference
import com.farhanrv.thestory.data.local.database.StoryDatabase
import com.farhanrv.thestory.data.network.NetworkDataSource
import com.farhanrv.thestory.data.network.api.ApiService
import com.farhanrv.thestory.ui.addstory.AddStoryViewModel
import com.farhanrv.thestory.ui.auth.login.LoginViewModel
import com.farhanrv.thestory.ui.auth.signup.SignupViewModel
import com.farhanrv.thestory.ui.main.MainViewModel
import com.farhanrv.thestory.ui.map.MapViewModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val databaseModule = module {
    factory { get<StoryDatabase>().storyDao() }
    single {
        Room.databaseBuilder(
            androidContext(),
            StoryDatabase::class.java,
            "StoryList.db"
        ).fallbackToDestructiveMigration()
            .build()
    }
}

val viewModelModule = module {
    viewModel { MainViewModel(get()) }
    viewModel { LoginViewModel(get()) }
    viewModel { SignupViewModel(get()) }
    viewModel { AddStoryViewModel(get()) }
    viewModel { MapViewModel(get()) }
}

val apiModule = module {
    single {
        OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .connectTimeout(120, TimeUnit.SECONDS)
            .readTimeout(120, TimeUnit.SECONDS)
            .build()
    }
    single {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://story-api.dicoding.dev/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(get())
            .build()
        retrofit.create(ApiService::class.java)
    }
}

val repositoryModule = module {
    single { NetworkDataSource(get()) }
    single { UserPreference(get()) }
    single { AppRepository(get(), get(), get(), get()) }
}
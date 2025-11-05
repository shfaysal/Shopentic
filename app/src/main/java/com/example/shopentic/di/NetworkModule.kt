package com.example.shopentic.di

import android.content.Context
import android.provider.Settings
import com.example.shopentic.BuildConfig
import com.example.shopentic.services.HomeApiService
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {


    @Provides
    fun provideBaseUrl(): String = BuildConfig.BASE_URL

    @Singleton
    @Provides
    fun provideApplicationContext(@ApplicationContext context: Context): Context = context

    @Singleton
    @Provides
    fun provideInterceptor(context: Context): Interceptor {
        return Interceptor { chain ->
            val request = chain.request().newBuilder()
                .addHeader("Authorization" , "Bearer lasdflasjdf")
            .build()
            chain.proceed(request)
        }
    }

    @Singleton
    @Provides
    fun providesHttpLoggingInterceptor() = HttpLoggingInterceptor()
        .apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

    val gson = GsonBuilder().setLenient().create()  // Allow lenient parsing

    //    @Singleton
//    @Provides
//    fun provideNetworkMonitor(@ApplicationContext context: Context): NetworkMonitor {
//        return NetworkMonitor(context)
//    }
//
    @Singleton
    @Provides
    fun providesOkHttpClient(
        authInterceptor: Interceptor,
        httpLoggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient = OkHttpClient
        .Builder()
        .addInterceptor(authInterceptor)
        .addInterceptor(httpLoggingInterceptor)
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .build()

    @Provides
    @Singleton
    fun provideRetrofit(baseUrl: String, okHttpClient: OkHttpClient): Retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .client(okHttpClient)
        .build()


    @Singleton
    @Provides
    fun provideHomeApiServices(retrofit: Retrofit): HomeApiService = retrofit.create(HomeApiService::class.java)

}

//fun getAndroidId(context: Context): String {
//    return Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
//}
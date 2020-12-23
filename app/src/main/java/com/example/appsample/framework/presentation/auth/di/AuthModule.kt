package com.example.appsample.framework.presentation.auth.di

import com.example.appsample.business.data.cache.abstraction.UserCacheDataSource
import com.example.appsample.business.data.cache.implementation.UserCacheDataSourceImpl
import com.example.appsample.business.data.network.abstraction.JsonPlaceholderApiSource
import com.example.appsample.business.domain.repository.abstraction.UserRepository
import com.example.appsample.business.domain.repository.implementation.UserRepositoryImpl
import com.example.appsample.business.interactors.common.GetUserUseCase
import com.example.appsample.framework.datasource.cache.abstraction.UserDaoService
import com.example.appsample.framework.datasource.cache.database.UserDao
import com.example.appsample.framework.datasource.cache.database.UserDatabase
import com.example.appsample.framework.datasource.cache.implementation.UserDaoServiceImpl
import com.example.appsample.framework.datasource.cache.mappers.CacheMapper
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val BASE_URL = "https://jsonplaceholder.typicode.com"

@Module
object AuthModule {

    @AuthFragmentScope
    @Provides
    fun provideRetrofitInstance(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build()
    }

    @AuthFragmentScope
    @Provides
    fun provideJsonPlaceholderApi(retrofit: Retrofit) = retrofit
        .create(JsonPlaceholderApiSource::class.java)

    @JvmStatic
    @AuthFragmentScope
    @Provides
    fun provideUserDAO(userDatabase: UserDatabase): UserDao {
        return userDatabase.userDao()
    }

    @JvmStatic
    @AuthFragmentScope
    @Provides
    fun provideUserCacheMapper(): CacheMapper {
        return CacheMapper()
    }

    @AuthFragmentScope
    @Provides
    fun provideUserDaoService(userDao: UserDao, cacheMapper: CacheMapper): UserDaoService {
        return UserDaoServiceImpl(userDao, cacheMapper)
    }

    @AuthFragmentScope
    @Provides
    fun provideUserCacheDataSourceImpl(userDaoService: UserDaoService): UserCacheDataSource {
        return UserCacheDataSourceImpl(userDaoService)
    }

    @AuthFragmentScope
    @Provides
    fun provideJsonPlaceholderRepository(
        jsonPlaceholderApiSource: JsonPlaceholderApiSource,
        userCacheDataSource: UserCacheDataSource
    ): UserRepository {
        return UserRepositoryImpl(userCacheDataSource, jsonPlaceholderApiSource)
    }

    @AuthFragmentScope
    @Provides
    fun provideGetUserUseCase(userRepository: UserRepository): GetUserUseCase {
        return GetUserUseCase(userRepository)
    }
}
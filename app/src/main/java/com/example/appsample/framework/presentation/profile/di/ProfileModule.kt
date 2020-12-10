package com.example.appsample.framework.presentation.profile.di

import com.example.appsample.business.data.network.abstraction.JsonPlaceholderApiSource
import com.example.appsample.business.domain.repository.abstraction.AlbumsRepository
import com.example.appsample.business.domain.repository.abstraction.UserRepository
import com.example.appsample.business.domain.repository.abstraction.PostsRepository
import com.example.appsample.business.domain.repository.implementation.AlbumsRepositoryImpl
import com.example.appsample.business.domain.repository.implementation.UserRepositoryImpl
import com.example.appsample.business.domain.repository.implementation.PostsRepositoryImpl
import com.example.appsample.business.interactors.profile.GetPostsUseCase
import com.example.appsample.business.interactors.common.GetUserUseCase
import com.example.appsample.business.interactors.profile.GetAlbumsUseCase
import com.example.appsample.framework.presentation.auth.di.BASE_URL
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
object ProfileModule {

    @ProfileFragmentScope
    @Provides
    fun provideRetrofitInstance(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build()
    }

    @ProfileFragmentScope
    @Provides
    fun provideJsonPlaceholderApi(retrofit: Retrofit) = retrofit
        .create(JsonPlaceholderApiSource::class.java)

    @ProfileFragmentScope
    @Provides
    fun provideJsonPlaceholderRepository(jsonPlaceholderApiSource: JsonPlaceholderApiSource): UserRepository {
        return UserRepositoryImpl(jsonPlaceholderApiSource)
    }

    @ProfileFragmentScope
    @Provides
    fun provideGetUserUseCase(userRepository: UserRepository): GetUserUseCase {
        return GetUserUseCase(userRepository)
    }

    @ProfileFragmentScope
    @Provides
    fun providePostsRepository(jsonPlaceholderApiSource: JsonPlaceholderApiSource): PostsRepository {
        return PostsRepositoryImpl(jsonPlaceholderApiSource)
    }

    @ProfileFragmentScope
    @Provides
    fun provideGetPostsUseCase(postsRepository: PostsRepository): GetPostsUseCase {
        return GetPostsUseCase(postsRepository)
    }

    @ProfileFragmentScope
    @Provides
    fun provideAlbumsRepository(jsonPlaceholderApiSource: JsonPlaceholderApiSource): AlbumsRepository {
        return AlbumsRepositoryImpl(jsonPlaceholderApiSource)
    }

    @ProfileFragmentScope
    @Provides
    fun provideGetAlbumsUseCase(albumsRepository: AlbumsRepository): GetAlbumsUseCase {
        return GetAlbumsUseCase(albumsRepository)
    }
}
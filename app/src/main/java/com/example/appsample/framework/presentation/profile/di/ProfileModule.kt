package com.example.appsample.framework.presentation.profile.di

import com.example.appsample.business.data.cache.abstraction.UserCacheDataSource
import com.example.appsample.business.data.cache.implementation.UserCacheDataSourceImpl
import com.example.appsample.business.data.network.abstraction.JsonPlaceholderApiSource
import com.example.appsample.business.domain.repository.abstraction.AlbumsRepository
import com.example.appsample.business.domain.repository.abstraction.CommentsRepository
import com.example.appsample.business.domain.repository.abstraction.PhotoRepository
import com.example.appsample.business.domain.repository.abstraction.PostsRepository
import com.example.appsample.business.domain.repository.abstraction.UserRepository
import com.example.appsample.business.domain.repository.implementation.AlbumsRepositoryImpl
import com.example.appsample.business.domain.repository.implementation.CommentsRepositoryImpl
import com.example.appsample.business.domain.repository.implementation.PhotoRepositoryImpl
import com.example.appsample.business.domain.repository.implementation.PostsRepositoryImpl
import com.example.appsample.business.domain.repository.implementation.UserRepositoryImpl
import com.example.appsample.business.interactors.common.GetUserUseCase
import com.example.appsample.business.interactors.profile.GetAlbumListUseCase
import com.example.appsample.business.interactors.profile.GetCommentListUseCase
import com.example.appsample.business.interactors.profile.GetPhotoListUseCase
import com.example.appsample.business.interactors.profile.GetPhotoUseCase
import com.example.appsample.business.interactors.profile.GetPostListUseCase
import com.example.appsample.business.interactors.profile.GetPostUseCase
import com.example.appsample.framework.datasource.cache.abstraction.UserDaoService
import com.example.appsample.framework.datasource.cache.database.UserDao
import com.example.appsample.framework.datasource.cache.database.UserDatabase
import com.example.appsample.framework.datasource.cache.implementation.UserDaoServiceImpl
import com.example.appsample.framework.datasource.cache.mappers.CacheMapper
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

    @JvmStatic
    @ProfileFragmentScope
    @Provides
    fun provideUserDAO(userDatabase: UserDatabase): UserDao {
        return userDatabase.userDao()
    }

    @JvmStatic
    @ProfileFragmentScope
    @Provides
    fun provideUserCacheMapper(): CacheMapper {
        return CacheMapper()
    }

    @ProfileFragmentScope
    @Provides
    fun provideUserDaoService(userDao: UserDao, cacheMapper: CacheMapper): UserDaoService {
        return UserDaoServiceImpl(userDao, cacheMapper)
    }

    @ProfileFragmentScope
    @Provides
    fun provideUserCacheDataSourceImpl(userDaoService: UserDaoService): UserCacheDataSource {
        return UserCacheDataSourceImpl(userDaoService)
    }

    @ProfileFragmentScope
    @Provides
    fun provideJsonPlaceholderRepository(
        jsonPlaceholderApiSource: JsonPlaceholderApiSource,
        userCacheDataSource: UserCacheDataSource
    ): UserRepository {
        return UserRepositoryImpl(userCacheDataSource, jsonPlaceholderApiSource)
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
    fun provideGetPostsUseCase(postsRepository: PostsRepository): GetPostListUseCase {
        return GetPostListUseCase(postsRepository)
    }

    @ProfileFragmentScope
    @Provides
    fun provideGetPostUseCase(postsRepository: PostsRepository): GetPostUseCase {
        return GetPostUseCase(postsRepository)
    }

    @ProfileFragmentScope
    @Provides
    fun provideAlbumsRepository(jsonPlaceholderApiSource: JsonPlaceholderApiSource): AlbumsRepository {
        return AlbumsRepositoryImpl(jsonPlaceholderApiSource)
    }

    @ProfileFragmentScope
    @Provides
    fun provideGetAlbumsUseCase(albumsRepository: AlbumsRepository): GetAlbumListUseCase {
        return GetAlbumListUseCase(albumsRepository)
    }

    @ProfileFragmentScope
    @Provides
    fun providePhotoRepository(jsonPlaceholderApiSource: JsonPlaceholderApiSource): PhotoRepository {
        return PhotoRepositoryImpl(jsonPlaceholderApiSource)
    }

    @ProfileFragmentScope
    @Provides
    fun provideGetPhotoUseCase(photoRepository: PhotoRepository): GetPhotoUseCase {
        return GetPhotoUseCase(photoRepository)
    }

    @ProfileFragmentScope
    @Provides
    fun provideGetPhotoListUseCase(photoRepository: PhotoRepository): GetPhotoListUseCase {
        return GetPhotoListUseCase(photoRepository)
    }

    @ProfileFragmentScope
    @Provides
    fun provideCommentsRepository(jsonPlaceholderApiSource: JsonPlaceholderApiSource): CommentsRepository {
        return CommentsRepositoryImpl(jsonPlaceholderApiSource)
    }

    @ProfileFragmentScope
    @Provides
    fun provideGetCommentListUseCase(commentsRepository: CommentsRepository): GetCommentListUseCase {
        return GetCommentListUseCase(commentsRepository)
    }
}
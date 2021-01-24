package com.example.appsample.framework.presentation.profile.di

import androidx.room.Room
import com.example.appsample.business.data.cache.abstraction.AlbumCacheDataSource
import com.example.appsample.business.data.cache.abstraction.CommentCacheDataSource
import com.example.appsample.business.data.cache.abstraction.PhotoCacheDataSource
import com.example.appsample.business.data.cache.abstraction.PostCacheDataSource
import com.example.appsample.business.data.cache.abstraction.UserCacheDataSource
import com.example.appsample.business.data.cache.implementation.AlbumCacheDataSourceImpl
import com.example.appsample.business.data.cache.implementation.CommentCacheDataSourceImpl
import com.example.appsample.business.data.cache.implementation.PhotoCacheDataSourceImpl
import com.example.appsample.business.data.cache.implementation.PostCacheDataSourceImpl
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
import com.example.appsample.business.interactors.profile.GetPostListUseCase
import com.example.appsample.business.interactors.profile.GetPostUseCase
import com.example.appsample.framework.base.presentation.BaseApplication
import com.example.appsample.framework.datasource.cache.abstraction.AlbumDaoService
import com.example.appsample.framework.datasource.cache.abstraction.CommentDaoService
import com.example.appsample.framework.datasource.cache.abstraction.PhotoDaoService
import com.example.appsample.framework.datasource.cache.abstraction.PostDaoService
import com.example.appsample.framework.datasource.cache.abstraction.UserDaoService
import com.example.appsample.framework.datasource.cache.database.AlbumDao
import com.example.appsample.framework.datasource.cache.database.AlbumDatabase
import com.example.appsample.framework.datasource.cache.database.CommentDao
import com.example.appsample.framework.datasource.cache.database.CommentDatabase
import com.example.appsample.framework.datasource.cache.database.PhotoDao
import com.example.appsample.framework.datasource.cache.database.PhotoDatabase
import com.example.appsample.framework.datasource.cache.database.PostDao
import com.example.appsample.framework.datasource.cache.database.PostDatabase
import com.example.appsample.framework.datasource.cache.database.UserDao
import com.example.appsample.framework.datasource.cache.database.UserDatabase
import com.example.appsample.framework.datasource.cache.implementation.AlbumDaoServiceImpl
import com.example.appsample.framework.datasource.cache.implementation.CommentDaoServiceImpl
import com.example.appsample.framework.datasource.cache.implementation.PhotoDaoServiceImpl
import com.example.appsample.framework.datasource.cache.implementation.PostDaoServiceImpl
import com.example.appsample.framework.datasource.cache.implementation.UserDaoServiceImpl
import com.example.appsample.framework.datasource.cache.mappers.AlbumCacheMapper
import com.example.appsample.framework.datasource.cache.mappers.CommentCacheMapper
import com.example.appsample.framework.datasource.cache.mappers.PhotoCacheMapper
import com.example.appsample.framework.datasource.cache.mappers.PostCacheMapper
import com.example.appsample.framework.datasource.cache.mappers.UserCacheMapper
import com.example.appsample.framework.presentation.auth.di.BASE_URL
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.InternalCoroutinesApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named

@FlowPreview
@ExperimentalCoroutinesApi
@InternalCoroutinesApi
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
    fun provideUserCacheMapper(): UserCacheMapper {
        return UserCacheMapper()
    }

    @ProfileFragmentScope
    @Provides
    fun provideUserDaoService(userDao: UserDao, userCacheMapper: UserCacheMapper): UserDaoService {
        return UserDaoServiceImpl(userDao, userCacheMapper)
    }

    @ProfileFragmentScope
    @Provides
    fun provideUserCacheDataSourceImpl(userDaoService: UserDaoService): UserCacheDataSource {
        return UserCacheDataSourceImpl(userDaoService)
    }

    @JvmStatic
    @ProfileFragmentScope
    @Provides
    fun providePostDb(app: BaseApplication): PostDatabase {
        return Room
            .databaseBuilder(app, PostDatabase::class.java, PostDatabase.DATABASE_NAME)
            .fallbackToDestructiveMigration()
            .build()
    }

    @JvmStatic
    @ProfileFragmentScope
    @Provides
    fun providePostDAO(postDatabase: PostDatabase): PostDao {
        return postDatabase.postDao()
    }

    @JvmStatic
    @ProfileFragmentScope
    @Provides
    fun providePostCacheMapper(): PostCacheMapper {
        return PostCacheMapper()
    }

    @ProfileFragmentScope
    @Provides
    fun providePostDaoService(postDao: PostDao, postCacheMapper: PostCacheMapper): PostDaoService {
        return PostDaoServiceImpl(postDao, postCacheMapper)
    }

    @ProfileFragmentScope
    @Provides
    fun providePostCacheDataSourceImpl(postDaoService: PostDaoService): PostCacheDataSource {
        return PostCacheDataSourceImpl(postDaoService)
    }

    @ProfileFragmentScope
    @Provides
    fun provideJsonPlaceholderRepository(
        @Named("DispatcherIO") ioDispatcher: CoroutineDispatcher,
        jsonPlaceholderApiSource: JsonPlaceholderApiSource,
        userCacheDataSource: UserCacheDataSource
    ): UserRepository {
        return UserRepositoryImpl(
            ioDispatcher,
            userCacheDataSource,
            jsonPlaceholderApiSource
        )
    }

    @ProfileFragmentScope
    @Provides
    fun provideGetUserUseCase(userRepository: UserRepository): GetUserUseCase {
        return GetUserUseCase(userRepository)
    }

    @ProfileFragmentScope
    @Provides
    fun providePostsRepository(
        @Named("DispatcherIO") ioDispatcher: CoroutineDispatcher,
        postCacheDataSource: PostCacheDataSource,
        jsonPlaceholderApiSource: JsonPlaceholderApiSource,
        commentsRepository: CommentsRepository
    ): PostsRepository {
        return PostsRepositoryImpl(
            ioDispatcher,
            postCacheDataSource,
            jsonPlaceholderApiSource,
            commentsRepository
        )
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

    @JvmStatic
    @ProfileFragmentScope
    @Provides
    fun provideAlbumDb(app: BaseApplication): AlbumDatabase {
        return Room
            .databaseBuilder(app, AlbumDatabase::class.java, AlbumDatabase.DATABASE_NAME)
            .fallbackToDestructiveMigration()
            .build()
    }

    @JvmStatic
    @ProfileFragmentScope
    @Provides
    fun provideAlbumDAO(albumDatabase: AlbumDatabase): AlbumDao {
        return albumDatabase.albumDao()
    }

    @JvmStatic
    @ProfileFragmentScope
    @Provides
    fun provideAlbumCacheMapper(): AlbumCacheMapper {
        return AlbumCacheMapper()
    }

    @ProfileFragmentScope
    @Provides
    fun provideAlbumDaoService(
        albumDao: AlbumDao,
        albumCacheMapper: AlbumCacheMapper
    ): AlbumDaoService {
        return AlbumDaoServiceImpl(albumDao, albumCacheMapper)
    }

    @ProfileFragmentScope
    @Provides
    fun provideAlbumCacheDataSourceImpl(albumDaoService: AlbumDaoService): AlbumCacheDataSource {
        return AlbumCacheDataSourceImpl(albumDaoService)
    }


    @ProfileFragmentScope
    @Provides
    fun provideAlbumsRepository(
        @Named("DispatcherIO") ioDispatcher: CoroutineDispatcher,
        mainDispatcher: CoroutineDispatcher,
        albumCacheDataSource: AlbumCacheDataSource,
        jsonPlaceholderApiSource: JsonPlaceholderApiSource,
        photoRepository: PhotoRepository
    ): AlbumsRepository {
        return AlbumsRepositoryImpl(
            mainDispatcher,
            ioDispatcher,
            albumCacheDataSource,
            jsonPlaceholderApiSource,
            photoRepository
        )
    }

    @ProfileFragmentScope
    @Provides
    fun provideGetAlbumsUseCase(albumsRepository: AlbumsRepository): GetAlbumListUseCase {
        return GetAlbumListUseCase(albumsRepository)
    }

    @JvmStatic
    @ProfileFragmentScope
    @Provides
    fun providePhotoDb(app: BaseApplication): PhotoDatabase {
        return Room
            .databaseBuilder(app, PhotoDatabase::class.java, PhotoDatabase.DATABASE_NAME)
            .fallbackToDestructiveMigration()
            .build()
    }

    @JvmStatic
    @ProfileFragmentScope
    @Provides
    fun providePhotoDAO(photoDatabase: PhotoDatabase): PhotoDao {
        return photoDatabase.photoDao()
    }

    @JvmStatic
    @ProfileFragmentScope
    @Provides
    fun providePhotoCacheMapper(): PhotoCacheMapper {
        return PhotoCacheMapper()
    }

    @ProfileFragmentScope
    @Provides
    fun providePhotoDaoService(
        photoDao: PhotoDao,
        photoCacheMapper: PhotoCacheMapper
    ): PhotoDaoService {
        return PhotoDaoServiceImpl(photoDao, photoCacheMapper)
    }

    @ProfileFragmentScope
    @Provides
    fun providePhotoCacheDataSourceImpl(photoDaoService: PhotoDaoService): PhotoCacheDataSource {
        return PhotoCacheDataSourceImpl(photoDaoService)
    }

    @ProfileFragmentScope
    @Provides
    fun providePhotoRepository(
        @Named("DispatcherIO") ioDispatcher: CoroutineDispatcher,
        photoCacheDataSource: PhotoCacheDataSource,
        jsonPlaceholderApiSource: JsonPlaceholderApiSource
    ): PhotoRepository {
        return PhotoRepositoryImpl(
            ioDispatcher,
            photoCacheDataSource,
            jsonPlaceholderApiSource
        )
    }

    @ProfileFragmentScope
    @Provides
    fun provideGetPhotoListUseCase(photoRepository: PhotoRepository): GetPhotoListUseCase {
        return GetPhotoListUseCase(photoRepository)
    }

    @JvmStatic
    @ProfileFragmentScope
    @Provides
    fun provideCommentDb(app: BaseApplication): CommentDatabase {
        return Room
            .databaseBuilder(app, CommentDatabase::class.java, CommentDatabase.DATABASE_NAME)
            .fallbackToDestructiveMigration()
            .build()
    }

    @JvmStatic
    @ProfileFragmentScope
    @Provides
    fun provideCommentDAO(commentDatabase: CommentDatabase): CommentDao {
        return commentDatabase.commentDao()
    }

    @JvmStatic
    @ProfileFragmentScope
    @Provides
    fun provideCommentCacheMapper(): CommentCacheMapper {
        return CommentCacheMapper()
    }

    @ProfileFragmentScope
    @Provides
    fun provideCommentDaoService(
        commentDao: CommentDao,
        commentCacheMapper: CommentCacheMapper
    ): CommentDaoService {
        return CommentDaoServiceImpl(commentDao, commentCacheMapper)
    }

    @ProfileFragmentScope
    @Provides
    fun provideCommentCacheDataSourceImpl(commentDaoService: CommentDaoService): CommentCacheDataSource {
        return CommentCacheDataSourceImpl(commentDaoService)
    }

    @ProfileFragmentScope
    @Provides
    fun provideCommentsRepository(
        @Named("DispatcherIO") ioDispatcher: CoroutineDispatcher,
        commentsCacheDataSource: CommentCacheDataSource,
        jsonPlaceholderApiSource: JsonPlaceholderApiSource
    ): CommentsRepository {
        return CommentsRepositoryImpl(
            ioDispatcher,
            commentsCacheDataSource,
            jsonPlaceholderApiSource
        )
    }

    @ProfileFragmentScope
    @Provides
    fun provideGetCommentListUseCase(commentsRepository: CommentsRepository): GetCommentListUseCase {
        return GetCommentListUseCase(commentsRepository)
    }
}
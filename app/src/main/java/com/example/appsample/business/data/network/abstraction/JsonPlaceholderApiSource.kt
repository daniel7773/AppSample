package com.example.appsample.business.data.network.abstraction

import com.example.appsample.business.data.models.AlbumEntity
import com.example.appsample.business.data.models.PhotoEntity
import com.example.appsample.business.data.models.PostEntity
import com.example.appsample.business.data.models.UserEntity
import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

const val GET_USER_TIMEOUT = 15000L
const val GET_ALBUMS_TIMEOUT = 15000L
const val GET_POSTS_TIMEOUT = 15000L

interface JsonPlaceholderApiSource {

    @GET("users/{id}")
    fun getUser(
        @Path("id") id: Int
    ): Deferred<UserEntity?>

    @GET("posts")
    fun getPostsListFromUser(
        @Query("userId") userId: Int
    ): Deferred<List<PostEntity>?>

    @GET("albums")
    fun getAlbumsFromUser(
        @Query("userId") userId: Int
    ): Deferred<List<AlbumEntity>?>

    @GET("albums")
    fun getAlbumPhotos(
        @Query("albumId") albumId: Int
    ): Deferred<List<PhotoEntity>?>

    @GET("photos/{id}")
    fun getPhotoById(
        @Path("id") photoId: Int // using hack in repository
    ): Deferred<PhotoEntity?>
}

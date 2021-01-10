package com.example.appsample.business.data.network.abstraction

import com.example.appsample.business.data.models.AlbumEntity
import com.example.appsample.business.data.models.CommentEntity
import com.example.appsample.business.data.models.PhotoEntity
import com.example.appsample.business.data.models.PostEntity
import com.example.appsample.business.data.models.UserEntity
import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface JsonPlaceholderApiSource {

    @GET("users/{id}")
    fun getUserAsync(
        @Path("id") id: Int
    ): Deferred<UserEntity?>

    @GET("posts")
    fun getPostsListFromUserAsync(
        @Query("userId") userId: Int
    ): Deferred<List<PostEntity>?>

    @GET("posts/{id}")
    fun getPostByIdAsync(
        @Path("id") postId: Int
    ): Deferred<PostEntity?>

    @GET("albums")
    fun getAlbumsFromUserAsync(
        @Query("userId") userId: Int
    ): Deferred<List<AlbumEntity>?>

    @GET("photos")
    fun getAlbumPhotosAsync(
        @Query("albumId") albumId: Int
    ): Deferred<List<PhotoEntity>?>

    @GET("photos/{id}")
    fun getPhotoByIdAsync(
        @Path("id") photoId: Int // using hack in repository
    ): Deferred<PhotoEntity?>

    @GET("comments")
    fun getCommentListByPostIdAsync(
        @Query("postId") postId: Int
    ): Deferred<List<CommentEntity>?>
}

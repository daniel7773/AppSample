package com.example.appsample.business.data.network.abstraction

import com.example.appsample.business.data.models.AlbumData
import com.example.appsample.business.data.models.CommentData
import com.example.appsample.business.data.models.PhotoData
import com.example.appsample.business.data.models.PostData
import com.example.appsample.business.data.models.UserData
import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface JsonPlaceholderApiSource {

    @GET("users/{id}")
    fun getUserAsync(
        @Path("id") id: Int
    ): Deferred<UserData?>

    @GET("posts")
    fun getPostsListFromUserAsync(
        @Query("userId") userId: Int
    ): Deferred<List<PostData>?>

    @GET("posts/{id}")
    fun getPostByIdAsync(
        @Path("id") postId: Int
    ): Deferred<PostData?>

    @GET("albums")
    fun getAlbumsFromUserAsync(
        @Query("userId") userId: Int
    ): Deferred<List<AlbumData>?>

    @GET("photos")
    fun getAlbumPhotosAsync(
        @Query("albumId") albumId: Int
    ): Deferred<List<PhotoData>?>

    @GET("photos/{id}")
    fun getPhotoByIdAsync(
        @Path("id") photoId: Int // using hack in repository
    ): Deferred<PhotoData?>

    @GET("comments")
    fun getCommentListByPostIdAsync(
        @Query("postId") postId: Int
    ): Deferred<List<CommentData>?>
}

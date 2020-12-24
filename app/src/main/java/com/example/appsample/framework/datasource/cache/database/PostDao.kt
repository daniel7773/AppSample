package com.example.appsample.framework.datasource.cache.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.appsample.framework.datasource.cache.model.PostCacheEntity

const val POST_PAGINATION_PAGE_SIZE = 5

@Dao
interface PostDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPost(post: PostCacheEntity): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertPosts(notes: List<PostCacheEntity>): LongArray

    @Query("SELECT * FROM posts WHERE id = :id")
    suspend fun searchPostById(id: Int): PostCacheEntity?

    @Query(
        """
        SELECT * FROM posts 
        WHERE title LIKE '%' || :query || '%' 
        OR body LIKE '%' || :query || '%' 
        ORDER BY updated_at DESC LIMIT (:page * :pageSize)
        """
    )
    suspend fun searchPosts(
        query: String,
        page: Int,
        pageSize: Int = POST_PAGINATION_PAGE_SIZE
    ): List<PostCacheEntity>

    @Query("DELETE FROM posts WHERE id IN (:ids)")
    suspend fun deletePosts(ids: List<Int>): Int

    @Query("DELETE FROM posts")
    suspend fun deleteAllPosts()

    @Query("SELECT * FROM posts WHERE user_id = :user_id")
    suspend fun getAllPosts(user_id: Int): List<PostCacheEntity>

    @Query(
        """
        UPDATE posts 
        SET 
        user_id = :user_id, 
        title = :title, 
        body = :body,
        updated_at = :updated_at
        WHERE id = :id
        """
    )
    suspend fun updatePost(
        id: Int,
        user_id: Int,
        title: String,
        body: String?,
        updated_at: String
    ): Int

    @Query("DELETE FROM posts WHERE id = :id")
    suspend fun deletePost(id: Int): Int

    @Query("SELECT COUNT(*) FROM posts WHERE user_id = :user_id")
    suspend fun getNumPosts(user_id: Int): Int
}













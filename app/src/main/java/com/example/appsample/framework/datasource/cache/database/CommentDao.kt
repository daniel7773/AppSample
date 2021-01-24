package com.example.appsample.framework.datasource.cache.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.appsample.framework.datasource.cache.model.CommentCacheEntity

const val COMMENT_PAGINATION_PAGE_SIZE = 10

@Dao
interface CommentDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertComment(comment: CommentCacheEntity): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertComments(comments: List<CommentCacheEntity>): LongArray

    @Query("SELECT * FROM comments WHERE id = :id")
    suspend fun searchCommentById(id: Int): CommentCacheEntity?

    @Query(
        """
        SELECT * FROM comments 
        WHERE name LIKE '%' || :query || '%' 
        OR body LIKE '%' || :query || '%' 
        ORDER BY updated_at DESC LIMIT (:page * :pageSize)
        """
    )
    suspend fun searchComments(
        query: String,
        page: Int,
        pageSize: Int = COMMENT_PAGINATION_PAGE_SIZE
    ): List<CommentCacheEntity>

    @Query("DELETE FROM comments WHERE id IN (:ids)")
    suspend fun deleteComments(ids: List<Int>): Int

    @Query("DELETE FROM comments")
    suspend fun deleteAllComments()

    @Query("SELECT * FROM comments WHERE post_id = :post_id")
    suspend fun getAllComments(post_id: Int): List<CommentCacheEntity>

    @Query(
        """
        UPDATE comments 
        SET 
        post_id = :post_id, 
        name = :name, 
        email = :email, 
        body = :body,
        updated_at = :updated_at
        WHERE id = :id
        """
    )
    suspend fun updateComment(
        id: Int,
        post_id: Int,
        name: String,
        email: String,
        body: String?,
        updated_at: String
    ): Int

    @Query("DELETE FROM comments WHERE id = :id")
    suspend fun deleteComment(id: Int): Int

    @Query("SELECT COUNT(*) FROM comments WHERE post_id = :post_id")
    suspend fun getNumComments(post_id: Int): Int
}













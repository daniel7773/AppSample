package com.example.appsample.framework.datasource.cache.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.appsample.framework.datasource.cache.model.UserCacheEntity

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserCacheEntity): Long

    @Query("SELECT * FROM user WHERE id = :id")
    suspend fun searchUserById(id: String): UserCacheEntity?

    @Query("DELETE FROM user WHERE id = :primaryKey")
    suspend fun deleteUser(primaryKey: String): Int

    @Query(
        """
        UPDATE user 
        SET 
        username = :username,
        email = :email,
        street = :street,
        suite = :suite,
        city = :city,
        zipcode = :zipcode,
        lat = :lat,
        lng = :lng,
        phone = :phone,
        website = :website,
        company_name = :company_name,
        catch_phrase = :catch_phrase,
        bs = :bs,
        updated_at = :updated_at
        WHERE id = :primaryKey
        """
    )
    suspend fun updateUser(
        primaryKey: String,
        username: String?,
        email: String?,
        street: String?,
        suite: String?,
        city: String?,
        zipcode: String?,
        lat: Double?,
        lng: Double?,
        phone: String,
        website: String,
        company_name: String,
        catch_phrase: String,
        bs: String,
        updated_at: String
    ): Int
}













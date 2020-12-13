package com.example.appsample.business.data.network

import com.example.appsample.business.data.models.AlbumEntity
import com.example.appsample.business.data.models.PostEntity
import com.example.appsample.business.data.models.UserEntity
import com.example.appsample.business.data.network.abstraction.JsonPlaceholderApiSource
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Deferred

// impossible values

const val FORCE_GET_EXCEPTION = 10000
const val FORCE_GET_TIMEOUT_EXCEPTION = 10001

class FakeJsonPlaceHolderApiSource : JsonPlaceholderApiSource {

    override fun getUser(id: Int): Deferred<UserEntity?> {
        val deferredUserEntity = CompletableDeferred<UserEntity>()
        if (id == FORCE_GET_EXCEPTION) {
            deferredUserEntity.completeExceptionally(Exception("Exception from FakeJsonPlaceHolderApiSource"))
        } else if (id == FORCE_GET_TIMEOUT_EXCEPTION) {
            // DO nothing, so timeout will happened
        } else {
            deferredUserEntity.complete(DataFactory.produceUserEntity())
        }

        return deferredUserEntity
    }

    override fun getPostsFromUser(
        userId: Int
    ): Deferred<List<PostEntity>?> {
        val deferredPostList = CompletableDeferred<List<PostEntity>?>()
        if (userId == FORCE_GET_EXCEPTION) {
            deferredPostList.completeExceptionally(Exception("Exception from FakeJsonPlaceHolderApiSource"))
        } else if (userId == FORCE_GET_TIMEOUT_EXCEPTION) {
            // DO nothing, so timeout will happened
        } else {
            deferredPostList.complete(DataFactory.produceListOfPostsEntity(4))
        }
        return deferredPostList
    }

    override fun getAlbumsFromUser(
        userId: Int
    ): Deferred<List<AlbumEntity>?> {
        val deferredAlbumList = CompletableDeferred<List<AlbumEntity>?>()
        if (userId == FORCE_GET_EXCEPTION) {
            deferredAlbumList.completeExceptionally(Exception("Exception from FakeJsonPlaceHolderApiSource"))
        } else if (userId == FORCE_GET_TIMEOUT_EXCEPTION) {
            // DO nothing, so timeout will happened
        } else {
            deferredAlbumList.complete(DataFactory.produceListOfAlbumsEntity(4))
        }
        return deferredAlbumList
    }

}
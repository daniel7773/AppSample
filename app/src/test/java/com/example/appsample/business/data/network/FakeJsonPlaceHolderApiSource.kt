package com.example.appsample.business.data.network

import com.example.appsample.business.data.models.AddressEntity
import com.example.appsample.business.data.models.AlbumEntity
import com.example.appsample.business.data.models.CompanyEntity
import com.example.appsample.business.data.models.GeoEntity
import com.example.appsample.business.data.models.PostEntity
import com.example.appsample.business.data.models.UserEntity
import com.example.appsample.business.data.network.abstraction.JsonPlaceholderApiSource
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Deferred

class FakeJsonPlaceHolderApiSource: JsonPlaceholderApiSource {

    override fun getUser(id: Int): Deferred<UserEntity?> {
        val deferredUserEntity = CompletableDeferred<UserEntity>()
        deferredUserEntity.complete(DataFactory.produceUserEntity())
        return deferredUserEntity
    }

    override fun getPostsFromUser(
        userId: Int
    ): Deferred<List<PostEntity>?> {
        val deferredPostList = CompletableDeferred<List<PostEntity>?>()
        deferredPostList.complete(DataFactory.produceListOfPosts(4))
        return deferredPostList
    }

    override fun getAlbumsFromUser(
        userId: Int
    ): Deferred<List<AlbumEntity>?> {
        val deferredAlbumList = CompletableDeferred<List<AlbumEntity>?>()
        deferredAlbumList.complete(DataFactory.produceListOfAlbums(4))
        return deferredAlbumList
    }

}
package com.example.appsample.business.data.network

import com.example.appsample.business.data.models.*

object DataFactory {

    fun produceUserEntity() = UserEntity(
        id = 0,
        username = "username",
        email = "email",
        address = AddressEntity(
            street = "street",
            suite = "suit",
            city = "city",
            zipcode = "zipcode",
            geo = GeoEntity(
                lat = 20.0,
                lng = 20.0
            )
        ),
        phone = "phone",
        website = "website",
        company = CompanyEntity(
            name = "name",
            catchPhrase = "catchPhrase",
            bs = "bs"
        )
    )

    fun produceListOfPosts(postsNum: Int): List<PostEntity> {
        val postList: ArrayList<PostEntity> = ArrayList()
        repeat(postsNum) {
            postList.add(producePostEntity(it))
        }
        return postList
    }

    fun producePostEntity(postId: Int) = PostEntity(
        id = postId,
        userId = 1,
        title = "title",
        body = "body"
    )

    fun produceListOfAlbums(albumNum: Int): List<AlbumEntity> {
        val albumList: ArrayList<AlbumEntity> = ArrayList()
        repeat(albumNum) {
            albumList.add(produceAlbumEntity(it))
        }
        return albumList
    }

    fun produceAlbumEntity(albumId: Int) = AlbumEntity(
        id = albumId,
        userId = 1,
        title = "title"
    )
}
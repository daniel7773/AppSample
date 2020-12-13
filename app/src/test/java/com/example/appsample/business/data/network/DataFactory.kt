package com.example.appsample.business.data.network

import com.example.appsample.business.data.models.AddressEntity
import com.example.appsample.business.data.models.AlbumEntity
import com.example.appsample.business.data.models.CompanyEntity
import com.example.appsample.business.data.models.GeoEntity
import com.example.appsample.business.data.models.PostEntity
import com.example.appsample.business.data.models.UserEntity
import com.example.appsample.business.domain.model.Address
import com.example.appsample.business.domain.model.Album
import com.example.appsample.business.domain.model.Company
import com.example.appsample.business.domain.model.Geo
import com.example.appsample.business.domain.model.Post
import com.example.appsample.business.domain.model.User
import com.example.appsample.business.domain.repository.abstraction.Resource
import com.example.appsample.framework.presentation.common.model.UserModel
import com.example.appsample.framework.presentation.profile.models.AddressModel
import com.example.appsample.framework.presentation.profile.models.CompanyModel
import com.example.appsample.framework.presentation.profile.models.GeoModel

object DataFactory {

    // Data layer

    fun produceUserEntity() = UserEntity(
        id = 2,
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

    fun produceListOfPostsEntity(postsNum: Int): List<PostEntity> {
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

    fun produceListOfAlbumsEntity(albumNum: Int): List<AlbumEntity> {
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


    // Domain layer

    fun <T> provideResourceSuccess(data: T) = Resource.Success<T>(data, "SUCCESS")
    fun <T> provideResourceError(data: T) = Resource.Error<T>(null, "ERROR", Exception("ERROR"))

    fun produceUser() = User(
        id = 2,
        username = "username",
        email = "email",
        address = Address(
            street = "street",
            suite = "suit",
            city = "city",
            zipcode = "zipcode",
            geo = Geo(
                lat = 20.0,
                lng = 20.0
            )
        ),
        phone = "phone",
        website = "website",
        company = Company(
            name = "name",
            catchPhrase = "catchPhrase",
            bs = "bs"
        )
    )

    fun produceListOfPosts(postsNum: Int): List<Post> {
        val postList: ArrayList<Post> = ArrayList()
        repeat(postsNum) {
            postList.add(producePost(it))
        }
        return postList
    }

    fun producePost(postId: Int) = Post(
        id = postId,
        userId = 1,
        title = "title",
        body = "body"
    )

    fun produceListOfAlbums(postsNum: Int): List<Album> {
        val postList: ArrayList<Album> = ArrayList()
        repeat(postsNum) {
            postList.add(produceAlbum(it))
        }
        return postList
    }

    fun produceAlbum(albumId: Int) = Album(
        id = albumId,
        userId = 1,
        title = "title"
    )

    // Presentation layer
    fun produceUserModel() = UserModel(
        id = 2,
        username = "username",
        email = "email",
        address = AddressModel(
            street = "street",
            suite = "suit",
            city = "city",
            zipcode = "zipcode",
            geoModel = GeoModel(
                lat = 20.0,
                lng = 20.0
            )
        ),
        phone = "phone",
        website = "website",
        company = CompanyModel(
            name = "name",
            catchPhrase = "catchPhrase",
            bs = "bs"
        )
    )
}
package com.example.appsample.business.data.network

import com.example.appsample.business.data.models.AddressData
import com.example.appsample.business.data.models.AlbumData
import com.example.appsample.business.data.models.CompanyData
import com.example.appsample.business.data.models.GeoData
import com.example.appsample.business.data.models.PhotoData
import com.example.appsample.business.data.models.PostData
import com.example.appsample.business.data.models.UserData
import com.example.appsample.business.domain.model.Address
import com.example.appsample.business.domain.model.Company
import com.example.appsample.business.domain.model.Album
import com.example.appsample.business.domain.model.Photo
import com.example.appsample.business.domain.model.Post
import com.example.appsample.business.domain.model.Geo
import com.example.appsample.business.domain.model.User

object DataFactory {

    // Data layer

    fun produceUserEntity() = UserData(
        id = 2,
        username = "username",
        email = "email",
        address = AddressData(
            street = "street",
            suite = "suit",
            city = "city",
            zipcode = "zipcode",
            geo = GeoData(
                lat = 20.0,
                lng = 20.0
            )
        ),
        phone = "phone",
        website = "website",
        company = CompanyData(
            name = "name",
            catchPhrase = "catchPhrase",
            bs = "bs"
        )
    )

    fun produceListOfPostsEntity(postsNum: Int): List<PostData> {
        val postList: ArrayList<PostData> = ArrayList()
        repeat(postsNum) {
            postList.add(producePostEntity(it))
        }
        return postList
    }

    fun producePostEntity(postId: Int) = PostData(
        id = postId,
        userId = 1,
        title = "title",
        body = "body"
    )

    fun produceListOfAlbumsEntity(albumNum: Int): List<AlbumData> {
        val albumList: ArrayList<AlbumData> = ArrayList()
        repeat(albumNum) {
            albumList.add(produceAlbumEntity(it))
        }
        return albumList
    }

    fun produceAlbumEntity(albumId: Int) = AlbumData(
        id = albumId,
        userId = 1,
        title = "title"
    )


    // Domain layer

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
    fun produceUserModel() = User(
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

    fun produceListOfPhotoEntity(photoNum: Int): List<PhotoData> {
        val albumList: ArrayList<PhotoData> = ArrayList()
        repeat(photoNum) {
            albumList.add(producePhotoEntity(it))
        }
        return albumList
    }

    fun producePhotoEntity(photoId: Int) = PhotoData(
        albumId = 1,
        id = photoId,
        title = "title",
        url = "url",
        thumbnailUrl = "thumbnailUrl"
    )

    fun produceListOfPhoto(photoNum: Int): List<Photo> {
        val albumList: ArrayList<Photo> = ArrayList()
        repeat(photoNum) {
            albumList.add(producePhoto(it))
        }
        return albumList
    }

    fun producePhoto(photoId: Int) = Photo(
        albumId = 1,
        id = photoId,
        title = "title",
        url = "url",
        thumbnailUrl = "thumbnailUrl"
    )

    fun producePhoto() = Photo(
        albumId = 1,
        id = 1,
        title = "title",
        url = "url",
        thumbnailUrl = "thumbnailUrl"
    )
}
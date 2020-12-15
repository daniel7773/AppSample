package com.example.appsample.business.domain.repository.abstraction

import com.example.appsample.business.domain.model.Photo

interface PhotoRepository {

    suspend fun getAlbumPhotoList(albumId: Int): Resource<List<Photo>?>

    suspend fun getPhotoById(albumId: Int, photoId: Int): Resource<Photo?>
}
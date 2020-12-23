package com.example.appsample.framework.presentation.profile.screens.album

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appsample.business.domain.repository.Resource
import com.example.appsample.business.interactors.profile.GetPhotoListUseCase
import com.example.appsample.framework.presentation.common.model.State
import com.example.appsample.framework.presentation.common.model.State.*
import com.example.appsample.framework.presentation.profile.mappers.PhotoToPhotoModelMapper
import com.example.appsample.framework.presentation.profile.model.PhotoModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope

private const val ALBUM_ID_KEY = "albumId"

@ExperimentalCoroutinesApi
class AlbumViewModel constructor( // I suppose it is better to use database instead of SavedStateHandle for complex data
    private val mainDispatcher: CoroutineDispatcher,
    private val getPhotoListUseCase: GetPhotoListUseCase,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _items: MutableLiveData<State<List<PhotoModel>?>> by lazy {
        MutableLiveData(Unknown())
    }

    val items: LiveData<List<PhotoModel>> = Transformations.map(_items) {
        when (it) {
            is Loading -> emptyList()
            is Success -> it.data
            is Error -> emptyList()
            is Unknown -> emptyList()
        }
    }

    private val _albumId: MutableLiveData<Int> = savedStateHandle.getLiveData(ALBUM_ID_KEY)
    val albumId: LiveData<Int> = _albumId

    val isLoading: LiveData<Boolean> = Transformations.map(_items) {
        return@map when (it) {
            is Loading -> true
            else -> false
        }
    }

    init {
        if (_albumId.value != null) { // it means viewModel is recreating and will not be called from fragment
            searchPhotos()
        }
    }

    fun isAlbumIdNull() = _albumId.value == null

    fun setAlbumId(albumId: Int) = savedStateHandle.set(ALBUM_ID_KEY, albumId)

    fun searchPhotos() {
        if (isAlbumIdNull()) throw Exception("albumId should not be NULL when starting search")
        val albumId = _albumId.value!!

        viewModelScope.launch(mainDispatcher) {
            supervisorScope {
                _items.value = when (val response = getPhotoListUseCase.getPhotoList(albumId)) {
                    is Resource.Success -> {
                        Log.d(TAG, "photos size ${response.data?.size}")
                        // force unwrap because null values must be handled earlier
                        val photoList = PhotoToPhotoModelMapper.mapPhotoList(response.data!!)
                        Success(photoList, response.message ?: "searchPhotos Success in ViewModel")
                    }
                    is Resource.Error -> {
                        Log.d(TAG, "searchPhotos error ${response.exception.localizedMessage}")
                        Error(response.message.toString(), response.exception)
                    }
                }
            }
        }
    }

    companion object {
        private const val TAG = "AlbumViewModel"
    }
}
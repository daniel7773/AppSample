package com.example.appsample.framework.presentation.profile.screens.photo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appsample.business.domain.model.Photo
import com.example.appsample.business.domain.state.DataState
import com.example.appsample.business.interactors.profile.GetPhotoListUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import javax.inject.Named

private const val ALBUM_ID_KEY = "albumId"

@ExperimentalCoroutinesApi
class PhotoViewModel constructor(
    private val mainDispatcher: CoroutineDispatcher,
    @Named("DispatcherIO") private val ioDispatcher: CoroutineDispatcher,
    private val getPhotoListUseCase: GetPhotoListUseCase,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _photoList: MutableLiveData<DataState<List<Photo>?>> by lazy {
        MutableLiveData(DataState.Idle())
    }

    val photoList: LiveData<DataState<List<Photo>?>> = _photoList
    private val _albumId: MutableLiveData<Int> = savedStateHandle.getLiveData(ALBUM_ID_KEY)
    val albumId: LiveData<Int> = _albumId

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

        viewModelScope.launch(ioDispatcher) { launchSearch(albumId) }
    }

    private suspend fun launchSearch(albumId: Int) {
        refreshData(DataState.Loading(null, "Loading..."))
        supervisorScope {
            getPhotoListUseCase.getPhotoList(albumId).collect { photoData ->
                refreshData(photoData)
            }
        }
    }

    private fun refreshData(state: DataState<List<Photo>?>) =
        viewModelScope.launch(mainDispatcher) {
            _photoList.value = state
        }

    companion object {
        private const val TAG = "PhotoViewModel"
    }
}
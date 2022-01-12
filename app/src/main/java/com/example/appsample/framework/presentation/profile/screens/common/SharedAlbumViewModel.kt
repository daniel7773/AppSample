package com.example.appsample.framework.presentation.profile.screens.common

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appsample.business.domain.model.Photo
import com.example.appsample.business.domain.state.DataState
import com.example.appsample.business.interactors.profile.GetPhotoListUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope

private const val ALBUM_ID_KEY = "albumId"

@InternalCoroutinesApi
class SharedAlbumViewModel : ViewModel() {

    private var mainDispatcher: CoroutineDispatcher? = null
    private var ioDispatcher: CoroutineDispatcher? = null

    private var getPhotoListUseCase: GetPhotoListUseCase? = null

    private var savedStateHandle: SavedStateHandle? = null // TODO: add this back

    fun initStuff(
        mainDispatcher: CoroutineDispatcher,
        ioDispatcher: CoroutineDispatcher,
        getPhotoListUseCase: GetPhotoListUseCase
    ) {
        this.mainDispatcher = mainDispatcher
        this.ioDispatcher = ioDispatcher


        this.getPhotoListUseCase = getPhotoListUseCase




        if (_albumId.value != null) { // it means viewModel is recreating and will not be called from fragment
            searchPhotos()
        }
    }


    val _selected = MutableLiveData(0)
    val selected: LiveData<Int> = _selected

    fun select(position: Int) {
        Log.d("Dasdas", "new position: ${position}")
        _selected.value = position
    }

    private val _photoList: MutableLiveData<DataState<List<Photo>?>> by lazy {
        MutableLiveData(DataState.Idle())
    }

    val photoList: LiveData<DataState<List<Photo>?>> = _photoList
    private val _albumId: MutableLiveData<Int> = MutableLiveData(1) // savedStateHandle.getLiveData(ALBUM_ID_KEY)
    val albumId: LiveData<Int> = _albumId

    fun isAlbumIdNull() = _albumId.value == null

    fun setAlbumId(albumId: Int) {
        _albumId.value = albumId
    } // requireNotNull(savedStateHandle).set(ALBUM_ID_KEY, albumId)

    fun searchPhotos() {
        if (isAlbumIdNull()) throw Exception("albumId should not be NULL when starting search")
        val albumId = _albumId.value!!

        viewModelScope.launch(requireNotNull(ioDispatcher)) { launchSearch(albumId) }
    }

    private suspend fun launchSearch(albumId: Int) {
        refreshData(DataState.Loading(null, "Loading..."))
        supervisorScope {

            requireNotNull(
                getPhotoListUseCase
            ).getPhotoList(albumId).collect { photoData ->

                refreshData(photoData)

            }


        }
    }

    private fun refreshData(state: DataState<List<Photo>?>) =
        viewModelScope.launch(requireNotNull(mainDispatcher)) {
            _photoList.value = state
        }
}
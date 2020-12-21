package com.example.appsample.framework.presentation.profile.screens.album

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appsample.business.domain.repository.abstraction.Resource
import com.example.appsample.business.interactors.profile.GetPhotoListUseCase
import com.example.appsample.framework.base.presentation.SessionManager
import com.example.appsample.framework.presentation.common.model.State
import com.example.appsample.framework.presentation.common.model.State.*
import com.example.appsample.framework.presentation.profile.mappers.PhotoToPhotoModelMapper
import com.example.appsample.framework.presentation.profile.models.PhotoModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import javax.inject.Inject

@ExperimentalCoroutinesApi
class AlbumViewModel @Inject constructor(
    private val mainDispatcher: CoroutineDispatcher,
    private val getPhotoListUseCase: GetPhotoListUseCase
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

    val isLoading: LiveData<Boolean> = Transformations.map(_items) {
        return@map when (it) {
            is Loading -> true
            else -> false
        }
    }

    fun searchPhotos(albumId: Int) {
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
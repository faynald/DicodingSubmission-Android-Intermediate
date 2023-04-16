package com.farhanrv.thestory.ui.map

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.farhanrv.thestory.data.AppRepository

class MapViewModel(private val repository: AppRepository) : ViewModel() {

    fun getAllStoriesWithMap(token: String) = repository.getAllStoriesWithMap(token).asLiveData()

}
package com.example.githatch.presentation.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.githatch.domain.usecase.GetContribsUseCase

class DetailViewModel(private val getContribsUseCase: GetContribsUseCase):ViewModel() {
    fun getContribs(ownerName:String, repoName:String) = liveData {
        val contribsList = getContribsUseCase.execute(ownerName, repoName)
        emit(contribsList)
    }
}


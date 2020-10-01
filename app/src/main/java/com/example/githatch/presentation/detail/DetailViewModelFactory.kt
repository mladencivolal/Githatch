package com.example.githatch.presentation.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.githatch.domain.usecase.GetContribsUseCase

@Suppress("UNCHECKED_CAST")
class DetailViewModelFactory(private val getContribsUseCase: GetContribsUseCase):ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return DetailViewModel(getContribsUseCase) as T
    }
}
package com.example.githatch.presentation.detail

import androidx.databinding.BindingAdapter
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.bumptech.glide.Glide
import com.example.githatch.domain.usecase.GetContribsUseCase
import de.hdodenhof.circleimageview.CircleImageView

class DetailViewModel(private val getContribsUseCase: GetContribsUseCase) : ViewModel() {
    fun getContribs(ownerName: String, repoName: String) = liveData {
        val contribsList = getContribsUseCase.execute(ownerName, repoName)
        emit(contribsList)
    }

    companion object {
        @JvmStatic
        @BindingAdapter("imageUrl")
        fun loadImage(view: CircleImageView, url: String?) = Glide.with(view).load(url).into(view)
    }
}


package com.example.githatch.presentation.owner

import androidx.databinding.BindingAdapter
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.bumptech.glide.Glide
import com.example.githatch.data.model.owner.Owner
import com.example.githatch.domain.usecase.GetAuthorUseCase
import com.example.githatch.domain.usecase.GetReposFromAuthorUseCase
import com.example.githatch.domain.usecase.LoadMoreReposFromAuthor
import de.hdodenhof.circleimageview.CircleImageView

class OwnerViewModel(
    private val getReposFromAuthorUseCase: GetReposFromAuthorUseCase,
    private val loadMoreReposFromAuthor: LoadMoreReposFromAuthor,
    private val getAuthorUseCase: GetAuthorUseCase
) : ViewModel() {
    fun getReposFromAuthor(ownerName: String) = liveData {
        val reposFromAuthor = getReposFromAuthorUseCase.execute(ownerName)
        emit(reposFromAuthor)
    }

    fun loadMoreReposFromAuthor() = liveData {
        val reposList = loadMoreReposFromAuthor.execute()
        emit(reposList)
    }

    fun getOwner(ownerName: String): LiveData<Owner> = liveData {
        val author = getAuthorUseCase.execute(ownerName)
        emit(author)
    }

    companion object {
        @JvmStatic
        @BindingAdapter("imageUrl")
        fun loadImage(view: CircleImageView, url: String?) = Glide.with(view).load(url).into(view)
    }
}
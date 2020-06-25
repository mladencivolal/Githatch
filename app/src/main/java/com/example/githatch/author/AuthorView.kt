package com.example.githatch.author

import com.example.githatch.api.model.Repository

interface AuthorView {
    fun attachRepos(repos: List<Repository>)

    fun showProgressBar()

    fun hideProgressBar()
}
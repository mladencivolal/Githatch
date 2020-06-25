package com.example.githatch.home

import com.example.githatch.api.model.Repository

interface HomeView {

    fun updateList(items: List<Repository>)

    fun clearList()

    fun showList()

    fun hideList()

    fun resetFilters()

    fun showProgressBar()

    fun hideProgressBar()
}
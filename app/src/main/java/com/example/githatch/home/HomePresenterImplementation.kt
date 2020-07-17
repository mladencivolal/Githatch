package com.example.githatch.home

import GetRepositories
import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.githatch.App
import com.example.githatch.api.model.Repository
import com.example.githatch.api.model.RetrofitFactory
import com.example.githatch.helpers.Helper
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response
import java.lang.Exception

class HomePresenterImplementation(private var context: Context) : HomePresenter {

    private var homeView: HomeView = context as HomeView

    private var searchPhrase: String = ""
    private var sortBy: String = ""
    private var orderBy: String = ""
    private var pageNum: Int = 0
    private var pageLength: Int = 10
    private var itemsCount: Int = 0
    private var totalCount: Int = 0

    private var isSortingApplied = false;

    private fun resetData() {
        homeView.clearList()

        pageLength = 10
        pageNum = 1

        totalCount = 0
        itemsCount = 0
    }

    private fun sortClear() {
        orderBy = ""
        sortBy = ""

        homeView.resetFilters()
    }

    private fun processResponse(response: GetRepositories) {
        totalCount = response!!.totalCount
        itemsCount += response!!.repos.size

        if (itemsCount != 0) {
            homeView.updateList(response!!.repos)
        }
    }

    private suspend fun getRepositoriesService() {
        val parentJob = Job()
        val coroutineScope = CoroutineScope(Dispatchers.Main + parentJob)
        val service = RetrofitFactory.makeRetrofitService()

        coroutineScope.launch(Dispatchers.IO) {
            val response = service.getRepos(searchPhrase, sortBy, orderBy, pageLength, pageNum)
            try {
                withContext(Dispatchers.Main) {
                    if (response.repos.isNotEmpty()) {
                        response.let {
                            processResponse(response)
                            homeView.hideProgressBar()
                        }
                    } else {
                        homeView.showProgressBar()
                    }
                }
            } catch (e: HttpException) {
                Log.e("REQUEST", "Exception ${e.message}")
            } catch (e: Throwable) {
                Log.e("REQUEST", "Ooops: Something else went wrong")
            }
        }
    }

    override suspend fun searchRepositories(searchPhrase: String, sortBy: String, orderBy: String) {
       Helper.hideKeyboard(context as Activity)

        if (this.searchPhrase != searchPhrase) {
            resetData()
        }

        this.searchPhrase = searchPhrase
        this.sortBy = sortBy
        this.orderBy = orderBy

        getRepositoriesService()
    }

    override suspend fun onSortDialogCancel() {
        if (isSortingApplied) {
            getRepositoriesService()
        }
    }

    override fun onFilterApply() {
            isSortingApplied = true
            resetData()
    }

    override suspend fun onFilterClear() {
        sortClear()
        resetData()
        getRepositoriesService()
    }

    override suspend fun onLoadMore() {
        if (itemsCount != 0 && totalCount != itemsCount) {
            pageNum++
            getRepositoriesService()
        }
    }
}
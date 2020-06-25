package com.example.githatch.home

import GetRepositories
import android.app.Activity
import android.content.Context
import android.widget.Toast
import com.example.githatch.App
import com.example.githatch.helpers.Helper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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

    private fun processResponse(response: Response<GetRepositories>) {
        totalCount = response.body()!!.totalCount
        itemsCount += response.body()!!.repos.size

        if (itemsCount != 0) {
            homeView.updateList(response.body()!!.repos)
        }
    }

    private fun getRepositoriesService() {
            App.apiService.getRepos(searchPhrase, sortBy, orderBy, pageLength, pageNum)
                .enqueue(object : Callback<GetRepositories> {
                    override fun onFailure(call: Call<GetRepositories>, t: Throwable) {
                        homeView.showProgressBar()
                    }

                    override fun onResponse(
                        call: Call<GetRepositories>,
                        response: Response<GetRepositories>
                    ) {
                        processResponse(response)
                        homeView.hideProgressBar()
                    }
                })
    }


    override fun searchRepositories(searchPhrase: String, sortBy: String, orderBy: String) {
       Helper.hideKeyboard(context as Activity)

        if (this.searchPhrase != searchPhrase) {
            resetData()
        }

        this.searchPhrase = searchPhrase
        this.sortBy = sortBy
        this.orderBy = orderBy

        getRepositoriesService()
    }

    override fun onSortDialogCancel() {
        if (isSortingApplied) {
            getRepositoriesService()
        }
    }

    override fun onFilterApply() {
            isSortingApplied = true
            resetData()
    }

    override fun onFilterClear() {
        sortClear()
        resetData()
        getRepositoriesService()
    }

    override fun onLoadMore() {
        if (itemsCount != 0 && totalCount != itemsCount) {
            pageNum++
            getRepositoriesService()
        }
    }
}
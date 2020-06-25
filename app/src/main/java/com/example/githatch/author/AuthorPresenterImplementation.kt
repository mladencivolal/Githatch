package com.example.githatch.author

import retrofit2.Callback


import android.content.Context
import com.example.githatch.App
import com.example.githatch.api.model.Repository
import retrofit2.Call
import retrofit2.Response

class AuthorPresenterImplementation(context: Context) : AuthorPresenter {
    private var authorView: AuthorView = context as AuthorView

    override fun getReposFromAuthor(name: String) {
        App.apiService.getReposFromAuthor(name).enqueue(object : Callback<List<Repository>> {
            override fun onFailure(call: retrofit2.Call<List<Repository>>, t: Throwable) {
                authorView.showProgressBar()
            }

            override fun onResponse(
                call: Call<List<Repository>>,
                response: Response<List<Repository>>
            ) {

                authorView.attachRepos(response.body()!!)
                authorView.hideProgressBar()
            }
        })
    }


}
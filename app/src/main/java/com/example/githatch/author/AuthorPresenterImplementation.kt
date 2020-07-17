package com.example.githatch.author

import retrofit2.Callback


import android.content.Context
import android.util.Log
import com.example.githatch.App
import com.example.githatch.api.model.Repository
import com.example.githatch.api.model.RetrofitFactory
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.HttpException
import retrofit2.Response

class AuthorPresenterImplementation(context: Context) : AuthorPresenter {
    private var authorView: AuthorView = context as AuthorView

    override fun getReposFromAuthor(name: String) {
        val parentJob = Job()
        val coroutineScope = CoroutineScope(Dispatchers.Main + parentJob)
        val service = RetrofitFactory.makeRetrofitService()

        coroutineScope.launch(Dispatchers.IO) {
            val response = service.getReposFromAuthor(name)
            try {
                withContext(Dispatchers.Main) {
                    if (response.isNotEmpty()) {
                        response.let {
                            authorView.attachRepos(response)
                        }
                    } else {
                        authorView.showProgressBar()
                    }
                }
            } catch (e: HttpException) {
                Log.e("REQUEST", "Exception ${e.message}")
            } catch (e: Throwable) {
                Log.e("REQUEST", "Ooops: Something else went wrong")
            }
        }
    }
}

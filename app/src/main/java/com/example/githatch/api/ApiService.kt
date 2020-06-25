package com.example.githatch.api

import GetRepositories
import com.example.githatch.api.model.Author
import com.example.githatch.api.model.Repository
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET(Constants.GET_REPOS)
    fun getRepos(
        @Query("q") q: String,
        @Query("sort") sort: String,
        @Query("order") order: String,
        @Query("per_page") per_page: Int,
        @Query("page") page: Int
    ): Call<GetRepositories>

    @GET(Constants.GET_REPOS_FROM_AUTHOR)
    fun getReposFromAuthor(@Path("authorName") authorName: String): Call<List<Repository>>

    @GET(Constants.GET_CONTRIBUTORS)
    fun getContributors(@Path("authorName") authorName: String, @Path("repoName") repoName: String): Call<List<Author>>
}
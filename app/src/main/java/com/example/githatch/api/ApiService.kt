package com.example.githatch.api

import GetRepositories
import com.example.githatch.api.model.Author
import com.example.githatch.api.model.Repository
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

//    @GET(Constants.GET_REPOS)
//    fun getRepos(
//        @Query("q") q: String,
//        @Query("sort") sort: String,
//        @Query("order") order: String,
//        @Query("per_page") per_page: Int,
//        @Query("page") page: Int
//    ): Call<GetRepositories>

    @GET(Constants.GET_REPOS)
    suspend fun getRepos(
        @Query("q") q: String,
        @Query("sort") sort: String,
        @Query("order") order: String,
        @Query("per_page") per_page: Int,
        @Query("page") page: Int
    ): GetRepositories

    @GET(Constants.GET_REPOS_FROM_AUTHOR)
    suspend fun getReposFromAuthor(@Path("authorName") authorName: String): List<Repository>

    @GET(Constants.GET_CONTRIBUTORS)
    suspend fun getContributors(@Path("authorName") authorName: String, @Path("repoName") repoName: String): List<Author>
}
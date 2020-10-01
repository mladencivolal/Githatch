package com.example.githatch.data.api

import com.example.githatch.data.model.owner.Owner
import com.example.githatch.data.model.repo.Repo
import com.example.githatch.data.model.repo.RepoList
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GitHubService {
    @GET("/search/repositories")
    suspend fun getRepos(
        @Query("q") q: String,
        @Query("sort") sort: String,
        @Query("order") order: String,
        @Query("per_page") per_page: Int,
        @Query("page") page: Int
    ): Response<RepoList>

    @GET("/repos/{ownerName}/{repoName}/contributors")
    suspend fun getContribs(
        @Path("ownerName") ownerName: String, @Path("repoName") repoName: String
    ): Response<List<Owner>>

    @GET("/users/{ownerName}/repos")
    suspend fun getReposFromAuthor(
        @Path("ownerName") ownerName: String,
        @Query("per_page") per_page: Int,
        @Query("page") page: Int
    ): Response<List<Repo>>
}
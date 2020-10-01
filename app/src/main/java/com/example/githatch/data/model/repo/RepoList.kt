package com.example.githatch.data.model.repo


import com.google.gson.annotations.SerializedName

data class RepoList(
    @SerializedName("incomplete_results")
    val incompleteResults: Boolean,
    @SerializedName("items")
    val repos: List<Repo>,
    @SerializedName("total_count")
    val totalCount: Int
)
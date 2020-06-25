package com.example.githatch.api

class Constants {
    companion object {
        const val MAIN_URL: String = "https://api.github.com"

        const val GET_REPOS: String = "/search/repositories"

        const val GET_CONTRIBUTORS: String = "/repos/{authorName}/{repoName}/contributors"

        const val GET_REPOS_FROM_AUTHOR: String = "/users/{authorName}/repos"

        const val REPO_DETAILS: String = "REPO_DETAILS"

        const val AUTHOR_DETAILS: String = "OWNER_DETAILS"
    }
}
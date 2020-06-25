package com.example.githatch.author

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githatch.R
import com.example.githatch.api.Constants
import com.example.githatch.api.model.Author
import com.example.githatch.api.model.Repository
import com.example.githatch.helpers.Helper
import com.example.githatch.home.RepositoriesAdapter
import com.example.githatch.repository.RepositoryActivity
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_author.*

class AuthorActivity: AppCompatActivity(), AuthorView, RepositoriesAdapter.OnItemClickListener, RepositoriesAdapter.OnLoadMoreListener {
    private lateinit var authorPresenter: AuthorPresenterImplementation
    private  lateinit var repositoryAdapter: RepositoriesAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_author)

        authorPresenter = AuthorPresenterImplementation(this)

        initRecyclerView()

        populateWithIntentData()
    }

    private fun populateWithIntentData() {
        val author = intent.getParcelableExtra<Author>(Constants.AUTHOR_DETAILS)

        tvAuthor.text = Helper.textFormatter("User ${author.login}", 4, "#FAFAFA")
        tvRepositories.text = "${author.login}'s repositories"

        Picasso.get().load(author.avatarUrl)
            .placeholder(R.mipmap.ic_launcher)
            .error(R.mipmap.ic_launcher)
            .into(ivRepoAuthor)

        ivLink.setOnClickListener { launchBrowserActivity(author) }

        authorPresenter.getReposFromAuthor(author.login)
    }

    private fun initRecyclerView() {
        recyclerview.layoutManager = LinearLayoutManager(this)
        recyclerview.itemAnimator = DefaultItemAnimator()
        ViewCompat.setNestedScrollingEnabled(recyclerview, false)

        repositoryAdapter = RepositoriesAdapter(false, recyclerview)
        repositoryAdapter.onItemClickListener = this
        repositoryAdapter.onLoadMoreListener = this
        recyclerview.adapter = repositoryAdapter
    }

    override fun attachRepos(repos: List<Repository>) {
        repositoryAdapter.updateRepos(repos)
    }

    override fun showProgressBar() {
        recyclerview.visibility = View.INVISIBLE
        progressBar.visibility = View.VISIBLE
    }

    override fun hideProgressBar() {
        recyclerview.visibility = View.VISIBLE
        progressBar.visibility = View.INVISIBLE    }

    override fun onItemClick(repo: Repository, v: View) {
        val intent = Intent(this, RepositoryActivity::class.java)
        intent.putExtra(Constants.REPO_DETAILS, repo)
        startActivity(intent)    }

    override fun onLoadMore() {
    }

    private fun launchBrowserActivity (author: Author) {
        val webpage: Uri = Uri.parse(author.htmlUrl)
        val intent = Intent(Intent.ACTION_VIEW, webpage)
        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        }
    }




}
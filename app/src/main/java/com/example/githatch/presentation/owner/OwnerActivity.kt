package com.example.githatch.presentation.owner

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.githatch.R
import com.example.githatch.data.model.owner.Owner
import com.example.githatch.data.model.repo.Repo
import com.example.githatch.databinding.ActivityOwnerBinding
import com.example.githatch.helpers.Helper
import com.example.githatch.presentation.detail.DetailActivity
import com.example.githatch.presentation.di.Injector
import com.example.githatch.presentation.repo.RepoAdapter
import javax.inject.Inject

class OwnerActivity : AppCompatActivity(), View.OnClickListener,
    RepoAdapter.OnItemClickListener, RepoAdapter.OnLoadMoreListener {
    @Inject
    lateinit var factory: OwnerViewModelFactory
    private lateinit var ownerViewModel: OwnerViewModel
    private lateinit var binding: ActivityOwnerBinding
    private lateinit var adapter: RepoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_owner)
        (application as Injector).createOwnerSubComponent()
            .inject(this)

        ownerViewModel = ViewModelProvider(this, factory)
            .get(OwnerViewModel::class.java)

        initRecyclerView()

        populateWithIntentData()
    }

    private fun initRecyclerView() {
        binding.recyclerview.layoutManager = LinearLayoutManager(this)
        adapter = RepoAdapter(binding.recyclerview, false)
        adapter.onItemClickListener = this
        adapter.onLoadMoreListener = this
        binding.recyclerview.adapter = adapter
    }

    private fun populateWithIntentData() {
        val author = intent.getParcelableExtra<Owner>("owner")

        binding.tvAuthor.text = Helper.textFormatter("User ${author!!.login}", 4, "#FAFAFA")
        binding.tvRepositories.text = "${author.login}'s repositories"

        val imageURL = author.avatarUrl
        Glide.with(binding.ivRepoAuthor.context)
            .load(imageURL)
            .into(binding.ivRepoAuthor)

        binding.ivLink.setOnClickListener { launchBrowserActivity(author) }

        getReposFromAuthor(author.login)
    }

    private fun getReposFromAuthor(ownerName: String) {

        binding.progressBar.visibility = View.VISIBLE

        val responseLiveData = ownerViewModel.getReposFromAuthor(ownerName)
        responseLiveData.observe(this, {
            if (it != null) {
                adapter.updateList(it)
                binding.progressBar.visibility = View.GONE
            } else {
                binding.progressBar.visibility = View.GONE
                Toast.makeText(applicationContext, "No data available", Toast.LENGTH_LONG).show()
            }
        })
    }

    override fun onClick(p0: View?) {
    }

    override fun onItemClick(repo: Repo, view:View) {
        launchDetailActivity(repo)
    }

    override fun onLoadMore() {
        val responseLiveData = ownerViewModel.loadMoreReposFromAuthor()
        responseLiveData.observe(this, {
            if (it != null) {
                adapter.setIsLoading(false)
               // if (it.size > 5) {
                    adapter.updateList(it)
                //}
                binding.progressBar.visibility = View.GONE
            }
        })
    }

    private fun launchDetailActivity(repo: Repo) {
        val intent = Intent(this, DetailActivity::class.java)
        intent.putExtra("repo", repo)
        startActivity(intent)
    }

    private fun launchBrowserActivity(author: Owner) {
        val webpage: Uri = Uri.parse(author.htmlUrl)
        val intent = Intent(Intent.ACTION_VIEW, webpage)
        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        }
    }
}
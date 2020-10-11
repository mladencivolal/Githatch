package com.example.githatch.presentation.detail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.example.githatch.R
import com.example.githatch.data.model.owner.Owner
import com.example.githatch.data.model.repo.Repo
import com.example.githatch.databinding.ActivityDetailBinding
import com.example.githatch.helpers.Helper
import com.example.githatch.presentation.di.Injector
import com.example.githatch.presentation.owner.OwnerActivity
import kotlinx.android.synthetic.main.activity_detail.*
import javax.inject.Inject

class DetailActivity : AppCompatActivity(),
    ContributorsAdapter.OnItemClickListener {
    @Inject
    lateinit var factory: DetailViewModelFactory
    private lateinit var detailViewModel: DetailViewModel
    private lateinit var binding: ActivityDetailBinding
    private lateinit var adapter: ContributorsAdapter
    private lateinit var repoData: Repo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail)
        (application as Injector).createDetailSubComponent()
            .inject(this)

        detailViewModel = ViewModelProvider(this, factory)
            .get(DetailViewModel::class.java)

        binding.fabUp.setOnClickListener{recyclerview.scrollToPosition(0)}

        initRecyclerView()

        populateWithIntentData()

        getContributors(repoData.owner.login, repoData.name)
    }

    private fun initRecyclerView() {
        binding.recyclerview.itemAnimator = DefaultItemAnimator()
        binding.recyclerview.layoutManager = GridLayoutManager(this, 4)
        ViewCompat.setNestedScrollingEnabled(binding.recyclerview, false)
        adapter = ContributorsAdapter()
        adapter.onItemClickListener = this
        binding.recyclerview.adapter = adapter
    }

    private fun populateWithIntentData() {
        repoData = intent.getParcelableExtra("repo")!!

        binding.tvTitle.text = repoData.name
        binding.tvAuthor.text = repoData.owner.login
        binding.tvLang.text = repoData.language
        binding.tvDateCreated.text = Helper.dateFormatter(repoData.createdAt)
        binding.tvDateUpdated.text = Helper.dateFormatter(repoData.updatedAt)
        binding.tvWatch.text = Helper.numberFormatter(repoData.watchersCount)
        binding.tvFork.text = Helper.numberFormatter(repoData.forksCount)
        binding.tvIssue.text = Helper.numberFormatter(repoData.openIssues)
        binding.tvDescription.text = repoData.description

        val imageURL = repoData.owner.avatarUrl
        Glide.with(binding.ivProfile.context)
            .load(imageURL)
            .into(binding.ivProfile)

        //binding.ivLink.setOnClickListener { launchBrowserActivity(repoData) }
        binding.ivProfile.setOnClickListener { launchOwnerActivity(repoData.owner) }
    }

    private fun getContributors(ownerName: String, repoName: String) {
        binding.progressBar.visibility = View.VISIBLE

        val responseLiveData = detailViewModel.getContribs(ownerName, repoName)
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

    override fun onItemClick(owner: Owner) {
        launchOwnerActivity(owner)
    }

    private fun launchOwnerActivity(owner: Owner) {
        val intent = Intent(this, OwnerActivity::class.java)
        intent.putExtra("owner", owner)
        startActivity(intent)
    }

    private fun launchBrowserActivity(repo: Repo) {
        val webpage: Uri = Uri.parse(repo.htmlUrl)
        val intent = Intent(Intent.ACTION_VIEW, webpage)
        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        }
    }
}




package com.example.githatch.presentation.detail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
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
        repoData = intent.getParcelableExtra<Repo>("repo")!!

        binding.tvTitle.text = repoData.name
        binding.tvAuthor.text = Helper.textFormatter("Author ${repoData.owner.login}", 6, "#FAFAFA")
        binding.tvLang.text = Helper.textFormatter("Language ${repoData.language}", 8, "#33BBFF")
        binding.tvDateCreated.text = Helper.textFormatter(
            "Initial Commit ${Helper.dateFormatter(repoData.createdAt)}",
            14,
            "#FAFAFA"
        )
        binding.tvDateUpdated.text = Helper.textFormatter(
            "Last Update ${Helper.dateFormatter(repoData.updatedAt)}",
            11,
            "#FAFAFA"
        )
        binding.tvWatch.text = Helper.numberFormatter(repoData.watchersCount)
        binding.tvFork.text = Helper.numberFormatter(repoData.forksCount)
        binding.tvIssue.text = Helper.numberFormatter(repoData.openIssues)
        binding.tvDescription.text = repoData.description

        val imageURL = repoData.owner.avatarUrl
        Glide.with(binding.ivProfile.context)
            .load(imageURL)
            .into(binding.ivProfile)

        binding.ivLink.setOnClickListener { launchBrowserActivity(repoData) }
        binding.ivProfile.setOnClickListener { launchOwnerActivity(repoData.owner) }
    }

    private fun getContributors(ownerName: String, repoName: String) {
        Log.i("MYTAG", "Activity: searchRepos ")

        binding.progressBar.visibility = View.VISIBLE

        val responseLiveData = detailViewModel.getContribs(ownerName, repoName)
        responseLiveData.observe(this, Observer {
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




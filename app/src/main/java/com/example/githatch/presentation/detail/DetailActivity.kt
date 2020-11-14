package com.example.githatch.presentation.detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
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
import com.example.githatch.helpers.visible
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

        initClickListeners()
        initRecyclerView()
        populateWithIntentData()

        getContributors(repoData.owner.login, repoData.name)
    }

    private fun initClickListeners() {
        binding.apply {
            fabUp.setOnClickListener { recyclerview.scrollToPosition(0) }
            ivProfile.setOnClickListener { launchOwnerActivity(repoData.owner) }
        }
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
        repoData = intent.getParcelableExtra(KEY_REPO)!!

        binding.apply {
            tvTitle.text = repoData.name
            tvAuthor.text = repoData.owner.login
            tvLang.text = repoData.language
            tvDateCreated.text = Helper.dateFormatter(repoData.createdAt)
            tvDateUpdated.text = Helper.dateFormatter(repoData.updatedAt)
            tvWatch.text = Helper.numberFormatter(repoData.watchersCount)
            tvFork.text = Helper.numberFormatter(repoData.forksCount)
            tvIssue.text = Helper.numberFormatter(repoData.openIssues)
            tvDescription.text = repoData.description
        }

        val imageURL = repoData.owner.avatarUrl
        Glide.with(binding.ivProfile.context)
            .load(imageURL)
            .into(binding.ivProfile)
    }

    private fun getContributors(ownerName: String, repoName: String) {
        binding.progressBar.visible(true)

        val responseLiveData = detailViewModel.getContribs(ownerName, repoName)
        responseLiveData.observe(this, {
            if (it != null) {
                adapter.updateList(it)
                binding.progressBar.visible(false)
            } else {
                binding.progressBar.visible(false)
                Toast.makeText(applicationContext, "No data available", Toast.LENGTH_LONG).show()
            }
        })
    }

    override fun onItemClick(owner: Owner) {
        launchOwnerActivity(owner)
    }

    private fun launchOwnerActivity(owner: Owner) {
        val intent = OwnerActivity.intent(this, owner)
        startActivity(intent)
    }

    companion object {
        const val KEY_REPO = "repo"

        fun intent(context: Context, repo: Repo): Intent {
            val intent = Intent(context, DetailActivity::class.java)
            intent.putExtra(KEY_REPO, repo)
            return intent
        }
    }
}




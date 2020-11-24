package com.example.githatch.presentation.detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
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

    companion object {
        const val KEY_REPO = "repo"

        fun intent(context: Context, repo: Repo): Intent =
            Intent(context, DetailActivity::class.java).putExtra(KEY_REPO, repo)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        repoData = intent.getParcelableExtra(KEY_REPO)!!

        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail)
        (application as Injector).createDetailSubComponent()
            .inject(this)
        binding.repo = repoData
        binding.helper = Helper.Companion

        detailViewModel = ViewModelProvider(this, factory)
            .get(DetailViewModel::class.java)

        binding.detailViewModel = detailViewModel

        initClickListeners()
        initRecyclerView()

        getContributors(repoData.owner.login, repoData.name)
    }

    private fun initClickListeners() {
        binding.apply {
            fabUp.setOnClickListener { recyclerview.scrollToPosition(0) }
            ivProfile.setOnClickListener { launchOwnerActivity(repoData.owner) }
        }
    }

    private fun initRecyclerView() = binding.apply {
        recyclerview.itemAnimator = DefaultItemAnimator()
        recyclerview.layoutManager = GridLayoutManager(this@DetailActivity, 4)
        adapter = ContributorsAdapter()
        adapter.onItemClickListener = this@DetailActivity
        recyclerview.setHasFixedSize(true)
        recyclerview.isNestedScrollingEnabled = false
        recyclerview.adapter = adapter
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

    override fun onItemClick(owner: Owner) = launchOwnerActivity(owner)

    private fun launchOwnerActivity(owner: Owner) = startActivity(OwnerActivity.intent(this, owner))
}




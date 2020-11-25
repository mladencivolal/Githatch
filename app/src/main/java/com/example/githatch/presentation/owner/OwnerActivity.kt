package com.example.githatch.presentation.owner

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githatch.R
import com.example.githatch.custom.RevealAnimator
import com.example.githatch.data.model.owner.Owner
import com.example.githatch.data.model.repo.Repo
import com.example.githatch.databinding.ActivityOwnerBinding
import com.example.githatch.helpers.visible
import com.example.githatch.presentation.detail.DetailActivity
import com.example.githatch.presentation.di.Injector
import com.example.githatch.presentation.repo.RepoAdapter
import javax.inject.Inject

class OwnerActivity : AppCompatActivity(), RepoAdapter.OnItemClickListener,
    RepoAdapter.OnLoadMoreListener {
    @Inject
    lateinit var factory: OwnerViewModelFactory
    private lateinit var ownerViewModel: OwnerViewModel
    private lateinit var binding: ActivityOwnerBinding
    private lateinit var adapter: RepoAdapter
    private lateinit var author: Owner
    private lateinit var revealAnimator: RevealAnimator

    private lateinit var alphaAnimation: Animation

    companion object {
        const val KEY_OWNER = "owner"
        fun intent(context: Context, owner: Owner): Intent =
            Intent(context, OwnerActivity::class.java).putExtra(KEY_OWNER, owner)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_owner)
        (application as Injector).createOwnerSubComponent()
            .inject(this)

        ownerViewModel = ViewModelProvider(this, factory)
            .get(OwnerViewModel::class.java)

        alphaAnimation = AnimationUtils.loadAnimation(this, R.anim.alpha_anim)
        revealAnimator = RevealAnimator
        revealAnimator.flag = true

        initClickListeners()
        initRecyclerView()
        populateWithIntentData()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        revealAnimator.flag = true
    }

    fun revealAnim(view: View) {
        revealAnimator.animate(
            binding.launchRevealAnimation,
            binding.firstFrame,
            binding.secondFrame,
            binding.secondFrameContent,
            this
        )
    }

    private fun initClickListeners() {
        binding.apply {
            ivLink.setOnClickListener { launchBrowserActivity(author.htmlUrl) }
            ivTwit.setOnClickListener { launchTwitterActivity(author) }
            fabUp.setOnClickListener { recyclerview.scrollToPosition(0) }
        }
    }

    private fun initRecyclerView() = binding.apply {
        recyclerview.layoutManager = LinearLayoutManager(this@OwnerActivity)
        adapter = RepoAdapter(recyclerview, false)
        adapter.onItemClickListener = this@OwnerActivity
        adapter.onLoadMoreListener = this@OwnerActivity
        recyclerview.setHasFixedSize(true)
        recyclerview.isNestedScrollingEnabled = false
        recyclerview.adapter = adapter
    }

    private fun populateWithIntentData() {
        val ownerName = intent.getParcelableExtra<Owner>(KEY_OWNER)!!.login
        val responseLiveData = ownerViewModel.getOwner(ownerName)
        responseLiveData.observe(this, {
            author = it
            binding.owner = author
            getReposFromAuthor(author.login)
        })
    }

    private fun getReposFromAuthor(ownerName: String) {
        binding.progressBar.visible(true)

        val responseLiveData = ownerViewModel.getReposFromAuthor(ownerName)
        responseLiveData.observe(this, {
            if (it != null) {
                adapter.setList(it)
                binding.progressBar.visible(false)
            } else binding.progressBar.visible(false)
        })
    }

    override fun onItemClick(repo: Repo, view: View) =
        startActivity(DetailActivity.intent(this, repo))

    override fun onLoadMore() {
        binding.progressBar.visible(true)
        val responseLiveData = ownerViewModel.loadMoreReposFromAuthor()
        responseLiveData.observe(this, {
            if (it != null) {
                adapter.setIsLoading(false)
                if (it.size > 5) adapter.updateList(it)
                adapter.setIsLoading(false)
                binding.progressBar.visible(false)
            }
        })
    }

    private fun launchBrowserActivity(htmlUrl: String) {
        if (htmlUrl.isNotEmpty()) {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(htmlUrl))
            if (intent.resolveActivity(packageManager) != null) startActivity(intent)
        }
    }

    private fun launchTwitterActivity(author: Owner) {
        if (!author.twitter.isNullOrEmpty()) {
            val link = resources.getString(R.string.twitter_link, author.twitter)
            val intent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse(link)
            ).setPackage(resources.getString(R.string.twitter_package))
            if (intent.resolveActivity(packageManager) != null) startActivity(intent)
            else launchBrowserActivity(link)
        }
    }
}
package com.example.githatch.presentation.owner

import android.animation.Animator
import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.ViewAnimationUtils
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.githatch.R
import com.example.githatch.data.model.owner.Owner
import com.example.githatch.data.model.repo.Repo
import com.example.githatch.databinding.ActivityOwnerBinding
import com.example.githatch.presentation.detail.DetailActivity
import com.example.githatch.presentation.di.Injector
import kotlinx.android.synthetic.main.activity_owner.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.hypot

class OwnerActivity : AppCompatActivity(), OwnerRepoAdapter.OnItemClickListener,
    OwnerRepoAdapter.OnLoadMoreListener {
    @Inject
    lateinit var factory: OwnerViewModelFactory
    private lateinit var ownerViewModel: OwnerViewModel
    private lateinit var binding: ActivityOwnerBinding
    private lateinit var adapter: OwnerRepoAdapter
    private lateinit var author: Owner

    lateinit var alphaAnimation: Animation
    private var pixelDensity = 0f
    private var flag = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_owner)
        (application as Injector).createOwnerSubComponent()
            .inject(this)

        ownerViewModel = ViewModelProvider(this, factory)
            .get(OwnerViewModel::class.java)

        pixelDensity = resources.displayMetrics.density
        alphaAnimation = AnimationUtils.loadAnimation(this, R.anim.alpha_anim)

        initClickListeners()
        initRecyclerView()
        populateWithIntentData()
    }

    fun revealAnim(view: View) {
        val imageButton = binding.launchRevealAnimation
        val revealView = binding.outerView
        val layoutContent = binding.layoutContent

        var x: Int = clAuthor.right
        val y: Int = clAuthor.bottom
        x -= (28 * pixelDensity + 16 * pixelDensity).toInt()
        val hypotenuse =
            hypot(clAuthor.width.toDouble(), clAuthor.height.toDouble()).toInt()
        if (flag) {
            imageButton.setImageResource(R.drawable.ic_cancel)
            val parameters = revealView.layoutParams as FrameLayout.LayoutParams
            parameters.height = clAuthor.height
            revealView.layoutParams = parameters
            val anim =
                ViewAnimationUtils.createCircularReveal(revealView, x, y, 0f, hypotenuse.toFloat())
            anim.duration = 700
            anim.addListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(animator: Animator) {}
                override fun onAnimationEnd(animator: Animator) {
                    layoutContent.visibility = View.VISIBLE
                    layoutContent.startAnimation(alphaAnimation)
                }

                override fun onAnimationCancel(animator: Animator) {}
                override fun onAnimationRepeat(animator: Animator) {}
            })
            revealView.visibility = View.VISIBLE
            anim.start()
            flag = false
        } else {
            imageButton.setImageResource(R.drawable.ic_info)
            val anim =
                ViewAnimationUtils.createCircularReveal(revealView, x, y, hypotenuse.toFloat(), 0f)
            anim.duration = 400
            anim.addListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(animator: Animator) {}
                override fun onAnimationEnd(animator: Animator) {
                    revealView.visibility = View.GONE
                    layoutContent.visibility = View.GONE
                }

                override fun onAnimationCancel(animator: Animator) {}
                override fun onAnimationRepeat(animator: Animator) {}
            })
            anim.start()
            flag = true
        }
    }

    private fun initClickListeners() {
        binding.apply {
            ivLink.setOnClickListener { launchBrowserActivity(author.htmlUrl) }
            ivTwit.setOnClickListener { launchTwitterActivity(author) }
            fabUp.setOnClickListener { recyclerview.scrollToPosition(0) }
        }
    }

    private fun initRecyclerView() {
        binding.recyclerview.layoutManager = LinearLayoutManager(this)
        adapter = OwnerRepoAdapter(binding.recyclerview)
        adapter.onItemClickListener = this
        adapter.onLoadMoreListener = this
        binding.recyclerview.adapter = adapter
    }

    @SuppressLint("SetTextI18n")
    private fun populateWithIntentData() {
        val authorName = intent.getParcelableExtra<Owner>("owner")!!.login

        CoroutineScope(Dispatchers.Main).launch {
            author = ownerViewModel.getAuthor(authorName)

            val imageURL = author.avatarUrl
            Glide.with(binding.ivRepoAuthor.context)
                .load(imageURL)
                .into(binding.ivRepoAuthor)

            binding.apply {
                tvAuthor.text = author.login
                tvBio.text = author.bio
                tvResults.text = author.login + "'s top rated repositories"
                tvLocation.text = author.location.toString()
                tvName.text = author.name
                tvEmail.text = if (author.email.isNullOrBlank()) "n/a" else author.email
                tvFollowers.text = author.followers.toString()
                tvFollowing.text = author.following.toString()
                tvRepos.text = author.publicRepos.toString()
                tvGists.text = author.publicGists.toString()
                tvBio.text = author.bio
                tvBioBack.text = author.bio
                tvLink.text = author.htmlUrl.substring(8)
                tvTwitter.text = if (author.twitter.isNullOrBlank()) "n/a" else author.twitter
            }

            getReposFromAuthor(author.login)
        }
    }

    private fun getReposFromAuthor(ownerName: String) {
        binding.progressBar.visible(true)

        val responseLiveData = ownerViewModel.getReposFromAuthor(ownerName)
        responseLiveData.observe(this, {
            if (it != null) {
                adapter.updateList(it)
                binding.progressBar.visible(false)
            } else {
                binding.progressBar.visible(false)
            }
        })
    }

    override fun onItemClick(repo: Repo, view: View) {
        launchDetailActivity(repo)
    }

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

    private fun launchDetailActivity(repo: Repo) {
        val intent = Intent(this, DetailActivity::class.java).putExtra("repo", repo)
        startActivity(intent)
    }

    private fun View.visible(show: Boolean) {
        visibility = if (show) View.VISIBLE else View.GONE
    }

    private fun launchBrowserActivity(htmlUrl: String) {
        val webpage: Uri = Uri.parse(htmlUrl)
        val intent = Intent(Intent.ACTION_VIEW, webpage)
        if (intent.resolveActivity(packageManager) != null) startActivity(intent)
    }

    private fun launchTwitterActivity(author: Owner) {
        val link = "https://twitter.com/${author.twitter}"
        val parsedLink: Uri = Uri.parse(link)
        val intent = Intent(Intent.ACTION_VIEW, parsedLink).setPackage("com.twitter.android")
        if (intent.resolveActivity(packageManager) != null) startActivity(intent)
        else launchBrowserActivity(link)
    }
}
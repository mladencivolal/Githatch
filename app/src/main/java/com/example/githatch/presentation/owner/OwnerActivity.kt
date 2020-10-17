package com.example.githatch.presentation.owner

import android.animation.Animator
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

class OwnerActivity : AppCompatActivity(), View.OnClickListener,
    OwnerRepoAdapter.OnItemClickListener, OwnerRepoAdapter.OnLoadMoreListener {
    @Inject
    lateinit var factory: OwnerViewModelFactory
    private lateinit var ownerViewModel: OwnerViewModel
    private lateinit var binding: ActivityOwnerBinding
    private lateinit var adapter: OwnerRepoAdapter
    private lateinit var author: Owner

    //REVEAL CARD
    lateinit var alphaAnimation: Animation
    var pixelDensity = 0f
    var flag = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_owner)
        (application as Injector).createOwnerSubComponent()
            .inject(this)

        ownerViewModel = ViewModelProvider(this, factory)
            .get(OwnerViewModel::class.java)

        binding.fabUp.setOnClickListener(this)

        initRecyclerView()

        populateWithIntentData()

        binding.ivLink.setOnClickListener { launchBrowserActivity(author.htmlUrl) }
        binding.ivTwit.setOnClickListener { launchTwitterActivity(author) }


        pixelDensity = resources.displayMetrics.density

        alphaAnimation = AnimationUtils.loadAnimation(this, R.anim.alpha_anim)
    }

    fun launchTwitter(view: View?) {
        val imageButton = binding.launchTwitterAnimation
        val revealView = binding.linearView
        val layoutContent = binding.layoutContent

        var x: Int = clAuthor.right
        val y: Int = clAuthor.bottom
        x -= (28 * pixelDensity + 16 * pixelDensity).toInt()
        val hypotenuse =
            hypot(clAuthor.width.toDouble(), clAuthor.height.toDouble()).toInt()
        if (flag) {
            //imageButton.setBackgroundResource(R.drawable.rounded_cancel_button)
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
            revealView.setVisibility(View.VISIBLE)
            anim.start()
            flag = false
        } else {
           // imageButton.setBackgroundResource(R.drawable.rounded_button)
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


    private fun initRecyclerView() {
        binding.recyclerview.layoutManager = LinearLayoutManager(this)
        adapter = OwnerRepoAdapter(binding.recyclerview)
        adapter.onItemClickListener = this
        adapter.onLoadMoreListener = this
        binding.recyclerview.adapter = adapter
    }

    private fun populateWithIntentData() {
        val authorName =  intent.getParcelableExtra<Owner>("owner")!!.login

        CoroutineScope(Dispatchers.Main).launch {
            author = ownerViewModel.getAuthor(authorName)

            binding.tvAuthor.text = author.login
            binding.tvBio.text = author.bio
            binding.tvResults.text = "${author.login}'s top rated repositories"
            binding.tvLocation.text = author.location.toString()

            val imageURL = author.avatarUrl
            Glide.with(binding.ivRepoAuthor.context)
                .load(imageURL)
                .into(binding.ivRepoAuthor)

            binding.tvName.text = author.name
            binding.tvEmail.text = if (author.email.isNullOrBlank()) "n/a" else author.email
            binding.tvFollowers.text = author.followers.toString()
            binding.tvFollowing.text = author.following.toString()
            binding.tvRepos.text = author.publicRepos.toString()
            binding.tvGists.text = author.publicGists.toString()
            binding.tvBio.text = author.bio
            binding.tvBioBack.text = author.bio
            binding.tvLink.text = author.htmlUrl.substring(8)
            binding.tvTwitter.text =if (author.twitter.isNullOrBlank()) "n/a" else author.twitter

            getReposFromAuthor(author.login)
        }
    }

    private fun getReposFromAuthor(ownerName: String) {

        binding.progressBar.visibility = View.VISIBLE

        val responseLiveData = ownerViewModel.getReposFromAuthor(ownerName)
        responseLiveData.observe(this, {
            if (it != null) {
                adapter.updateList(it)
                binding.progressBar.visibility = View.INVISIBLE
            } else {
                binding.progressBar.visibility = View.INVISIBLE
                Toast.makeText(applicationContext, "No data available", Toast.LENGTH_LONG).show()
            }
        })
    }

    override fun onClick(p0: View?) {
        when (p0!!.id) {
            R.id.fabUp -> recyclerview.scrollToPosition(0)
        }
    }

    override fun onItemClick(repo: Repo, view: View) {
        launchDetailActivity(repo)
    }

    override fun onLoadMore() {
        binding.progressBar.visibility = View.VISIBLE
        val responseLiveData = ownerViewModel.loadMoreReposFromAuthor()
        responseLiveData.observe(this, {
            if (it != null) {
                adapter.setIsLoading(false)
                if (it.size > 5) {
                    adapter.updateList(it)
                }
                adapter.setIsLoading(false)
                binding.progressBar.visibility = View.INVISIBLE
            }
        })
    }

    private fun launchDetailActivity(repo: Repo) {
        val intent = Intent(this, DetailActivity::class.java)
        intent.putExtra("repo", repo)
        startActivity(intent)
    }

    private fun launchBrowserActivity(htmlUrl: String) {
        val webpage: Uri = Uri.parse(author.htmlUrl)
        val intent = Intent(Intent.ACTION_VIEW, webpage)
        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        }
    }

    private fun launchTwitterActivity(author: Owner) {
        var link = "https://twitter.com/${author.twitter}"
       val parsedLink: Uri = Uri.parse(link)
        val intent = Intent(Intent.ACTION_VIEW, parsedLink)
        intent.setPackage("com.twitter.android")
        if(intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        } else {
            launchBrowserActivity(link)
        }
    }
}
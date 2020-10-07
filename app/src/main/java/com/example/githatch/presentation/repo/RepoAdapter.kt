package com.example.githatch.presentation.repo

import android.animation.AnimatorInflater
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.githatch.R
import com.example.githatch.data.model.repo.Repo
import com.example.githatch.databinding.LayoutItemRepositoryBinding
import com.example.githatch.helpers.Helper
import kotlinx.android.synthetic.main.layout_item_repository.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class RepoAdapter(recyclerView: RecyclerView, private var isRepoActivity: Boolean) :
    RecyclerView.Adapter<RepoAdapter.MyViewHolder>() {
    private val repoList: MutableList<Repo> = mutableListOf()
    private var loading: Boolean = false
    lateinit var onItemClickListener: OnItemClickListener
    lateinit var onLoadMoreListener: OnLoadMoreListener

    init {
        var buffer = 0
        var pageLength = 5

        if (isRepoActivity) {
            buffer = 0
            pageLength = 10
        }

        if (recyclerView.layoutManager is LinearLayoutManager) {
            val linearLayoutManager = recyclerView.layoutManager as LinearLayoutManager

            recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)

                    val totalItemCount = linearLayoutManager.itemCount
                    val lastVisibleItem =
                        linearLayoutManager.findLastCompletelyVisibleItemPosition() + buffer

                    if (!loading && totalItemCount - 1 <= lastVisibleItem && lastVisibleItem > repoList.size - pageLength) {
                        onLoadMoreListener.onLoadMore()
                        loading = true
                    }
                }
            })
        }
    }

    fun setList(movies: List<Repo>) {
        repoList.clear()
        repoList.addAll(movies)
        notifyDataSetChanged()
    }

    fun updateList(repos: List<Repo>) {
        repos.forEach { this.repoList.add(it) }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: LayoutItemRepositoryBinding = DataBindingUtil.inflate(
            layoutInflater,
            R.layout.layout_item_repository,
            parent,
            false
        )
        return MyViewHolder(binding, isRepoActivity)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(repoList[position])
    }

    override fun getItemCount() = repoList.size

    fun setIsLoading(isLoading: Boolean) {
        loading = isLoading
    }

    interface OnLoadMoreListener {
        fun onLoadMore()
    }

    interface OnItemClickListener {
        fun onItemClick(repo: Repo, view:View)
    }

    inner class MyViewHolder(
        val binding: LayoutItemRepositoryBinding,
        private val isRepoActivity: Boolean
    ) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        init {
            itemView.setOnClickListener(this)
        }

        fun bind(repo: Repo) {
            binding.myFlipView.setFlipped(false)
            binding.tvRepoName.text = repo.name
            binding.tvRepoName.isSelected = true
            binding.tvAuthorName.text = Helper.textFormatter(
                "Author: ${repo.owner.login}",
                8,
                "#FAFAFA"
            )
            binding.tvWatch.text = Helper.numberFormatter(repo.watchersCount)
            binding.tvIssue.text = Helper.numberFormatter(repo.openIssues)
            binding.tvFork.text = Helper.numberFormatter(repo.forksCount)
            binding.tvLanguage.text = repo.language

            if (isRepoActivity) {
                val imageURL = repo.owner.avatarUrl
                Glide.with(binding.ivRepoAuthor.context)
                    .load(imageURL)
                    .into(binding.ivRepoAuthor)
            } else {
                binding.ivRepoAuthor.visibility = View.GONE
            }

            binding.root.findViewById<ImageView>(R.id.ivRepoAuthor).setOnClickListener(View.OnClickListener {
                onItemClickListener.onItemClick(repoList[bindingAdapterPosition], binding.root.findViewById<ImageView>(R.id.ivRepoAuthor))
            })
            binding.root.findViewById<TextView>(R.id.tvRepoName).setOnClickListener(View.OnClickListener {
                onItemClickListener.onItemClick(repoList[bindingAdapterPosition], binding.root.findViewById<ImageView>(R.id.tvRepoName))
            })
        }

        override fun onClick(v: View?) {
            when (v) {
                binding.myFlipView -> {
                    binding.myFlipView.flip()
                    CoroutineScope(Dispatchers.Main).launch {
                        if (binding.myFlipView.isFlipped()) {
                            delay(300)
                            binding.tvDescription.text =
                                repoList[bindingAdapterPosition].description.toString()
                            binding.ivRepoAuthor.isClickable = false
                            binding.tvRepoName.isClickable = false
                        } else {
                            delay(300)
                            binding.ivRepoAuthor.isClickable = true
                            binding.tvRepoName.isClickable = true
                            binding.tvDescription.text = ""
                        }
                    }
                }
            }
        }
    }
}
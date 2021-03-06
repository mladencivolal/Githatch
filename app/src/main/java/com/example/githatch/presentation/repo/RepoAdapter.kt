package com.example.githatch.presentation.repo

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.githatch.R
import com.example.githatch.data.model.repo.Repo
import com.example.githatch.databinding.LayoutItemRepositoryBinding
import com.example.githatch.helpers.Helper
import com.example.githatch.helpers.visible
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
    lateinit var binding: LayoutItemRepositoryBinding

    init {
        val pageLength = 5

        if (recyclerView.layoutManager is LinearLayoutManager) {
            val linearLayoutManager = recyclerView.layoutManager as LinearLayoutManager

            recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)

                    val totalItemCount = linearLayoutManager.itemCount
                    val lastVisibleItem =
                        linearLayoutManager.findLastCompletelyVisibleItemPosition()
                    if (dy > 0 && totalItemCount > pageLength && !loading && totalItemCount - 1 <= lastVisibleItem + pageLength && lastVisibleItem > repoList.size - 2 * pageLength) {
                        Log.i("MYTAG", "RepoAdapter: onLoadMoreListener triggered")
                        onLoadMoreListener.onLoadMore()
                        loading = true
                    }
                }
            })
        }
    }

    fun shrinkIfNotRepoActivity() =
        binding.apply {
            tvRepoName.textSize = 13.0f
            listOf(lbWatch, lbFork, lbUpdated, tvWatch, tvFork, tvDateUpdated, tvLanguage).forEach {
                it.textSize = 12.0f
            }
            ivRepoAuthor.visible(false)
            tvAuthorName.visible(false)
            lbAuthor.visible(false)
        }

    override fun onViewRecycled(holder: MyViewHolder) {
        holder.binding.executePendingBindings()
        Glide.with(binding.ivRepoAuthor.context).clear(holder.binding.ivRepoAuthor)
        holder.binding.ivRepoAuthor.setImageDrawable(null)
        super.onViewRecycled(holder)
    }

    fun setList(repos: List<Repo>) {
        repoList.clear()
        repoList.addAll(repos)
        notifyDataSetChanged()
    }

    fun updateList(repos: List<Repo>) {
        repos.forEach {
            this.repoList.add(it)
            notifyItemInserted(repoList.indexOf(it))
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        binding = DataBindingUtil.inflate(
            layoutInflater,
            R.layout.layout_item_repository,
            parent,
            false
        )
        return MyViewHolder(binding, isRepoActivity)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) =
        holder.bind(repoList[position])

    override fun getItemCount() = repoList.size

    fun setIsLoading(isLoading: Boolean) {
        loading = isLoading
    }

    interface OnLoadMoreListener {
        fun onLoadMore()
    }

    interface OnItemClickListener {
        fun onItemClick(repo: Repo, view: View)
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
            binding.repo = repo
            binding.helper = Helper.Companion
            binding.apply {
                myFlipView.setFlipped(false)
                val clickers = listOf(ivRepoAuthor, tvRepoName)
                clickers.toTypedArray().forEach { view ->
                    view.setOnClickListener {
                        onItemClickListener.onItemClick(
                            repoList[bindingAdapterPosition], it
                        )
                    }
                }
            }

            if (isRepoActivity) {
                val imageURL = repo.owner.avatarUrl
                Glide.with(binding.ivRepoAuthor.context)
                    .load(imageURL)
                    .into(binding.ivRepoAuthor)
                binding.tvAuthorName.text = repo.owner.login
            } else {
                shrinkIfNotRepoActivity()
            }
        }

        override fun onClick(v: View?) {
            when (v) {
                binding.myFlipView -> {
                    binding.myFlipView.flip()
                    CoroutineScope(Dispatchers.Main).launch {
                        if (binding.myFlipView.isFlipped()) {
                            delay(300)
                            binding.itemBack.visible(true)
                            frontClickable(false)
                        } else {
                            delay(300)
                            binding.itemBack.visible(false)
                            frontClickable(true)
                        }
                    }
                }
            }
        }

        private fun frontClickable(clickable: Boolean) {
            binding.apply {
                ivRepoAuthor.isClickable = clickable
                tvRepoName.isClickable = clickable
            }
        }
    }
}
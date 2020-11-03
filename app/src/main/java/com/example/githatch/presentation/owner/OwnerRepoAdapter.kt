package com.example.githatch.presentation.owner

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.githatch.R
import com.example.githatch.data.model.repo.Repo
import com.example.githatch.databinding.LayoutItemRepositoryBinding
import com.example.githatch.helpers.Helper
import com.example.githatch.helpers.visible
import kotlinx.android.synthetic.main.layout_item_repository.view.*
import kotlinx.android.synthetic.main.layout_sort_sheet.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class OwnerRepoAdapter(recyclerView: RecyclerView) :
    RecyclerView.Adapter<OwnerRepoAdapter.MyViewHolder>() {
    private val repoList: MutableList<Repo> = mutableListOf()
    private var loading: Boolean = false
    lateinit var onItemClickListener: OnItemClickListener
    lateinit var onLoadMoreListener: OnLoadMoreListener

    init {

        val pageLength = 5

        if (recyclerView.layoutManager is LinearLayoutManager) {
            val linearLayoutManager: LinearLayoutManager =
                recyclerView.layoutManager as LinearLayoutManager

            recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)

                    val totalItemCount = linearLayoutManager.itemCount
                    val lastVisibleItem =
                        linearLayoutManager.findLastCompletelyVisibleItemPosition()

                    if (!loading && totalItemCount - 1 <= lastVisibleItem && lastVisibleItem > repoList.size - pageLength) {
                        onLoadMoreListener.onLoadMore()
                        loading = true
                    }
                }
            })
        }
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
        return MyViewHolder(binding)
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
        fun onItemClick(repo: Repo, view: View)
    }

    inner class MyViewHolder(
        val binding: LayoutItemRepositoryBinding
    ) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        init {
            itemView.setOnClickListener(this)
            shrinkTextSize()
        }

        fun shrinkTextSize() {
            binding.apply {
                tvRepoName.textSize = 13.0f
                tvLanguage.textSize = 12.0f
                listOf(lbWatch, lbFork, lbUpdated, tvWatch, tvFork, tvDateUpdated).forEach {
                    it.textSize = 12.0f
                }
            }
        }

        fun bind(repo: Repo) {
            binding.apply {
                myFlipView.setFlipped(false)
                tvRepoName.text = repo.name
                tvWatch.text = Helper.numberFormatter(repo.watchersCount)
                tvDateUpdated.text = Helper.dateFormatterAlt(repo.updatedAt)
                tvFork.text = Helper.numberFormatter(repo.forksCount)
                tvLanguage.text = repo.language
                binding.tvDescription.text =
                    repoList[bindingAdapterPosition].description.toString()
                val clickers = listOf(ivRepoAuthor, tvRepoName)
                clickers.toTypedArray().forEach { view ->
                    view.setOnClickListener {
                        onItemClickListener.onItemClick(
                            repoList[bindingAdapterPosition], it
                        )
                    }
                }
                ivRepoAuthor.visible(false)
                tvAuthorName.visible(false)
                lbAuthor.visible(false)
                itemBack.visible(false)
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
                            flippedSideClickable(false)
                        } else {
                            delay(300)
                            binding.itemBack.visible(false)
                            flippedSideClickable(true)
                        }
                    }
                }
            }
        }

        private fun flippedSideClickable(clickable: Boolean) {
            binding.apply {
                tvDescription.text =
                    repoList[bindingAdapterPosition].description.toString()
                ivRepoAuthor.isClickable = clickable
                tvRepoName.isClickable = clickable
            }
        }
    }
}


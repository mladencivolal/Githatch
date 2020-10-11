package com.example.githatch.presentation.owner

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.githatch.R
import com.example.githatch.data.model.repo.Repo
import com.example.githatch.databinding.LayoutItemRepositoryBinding
import com.example.githatch.helpers.Helper

class OwnerRepoAdapter (recyclerView: RecyclerView) :
    RecyclerView.Adapter<OwnerRepoAdapter.MyViewHolder>() {
    private val repoList: MutableList<Repo> = mutableListOf()
    private var loading: Boolean = false
    lateinit var onItemClickListener: OnItemClickListener
    lateinit var onLoadMoreListener: OnLoadMoreListener

    init {
        var buffer = 0
        var pageLength = 5

        if (recyclerView.layoutManager is LinearLayoutManager) {
            val linearLayoutManager = recyclerView.layoutManager as LinearLayoutManager

            recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)

                    val totalItemCount = linearLayoutManager.itemCount
                    val lastVisibleItem =
                        linearLayoutManager.findLastCompletelyVisibleItemPosition()

                    Log.i("MYTAG", "ownerrepoadapter")
                    Log.i("MYTAG", "loading: $loading ")
                    Log.i("MYTAG", "total num of adapter items: ${totalItemCount-1} ")
                    Log.i("MYTAG", "lastvisibleitem position: $lastVisibleItem ")
                    Log.i("MYTAG", "repoList size: ${repoList.size} ")

                     if (!loading && totalItemCount - 1 <= lastVisibleItem && lastVisibleItem > repoList.size - pageLength) {
                        onLoadMoreListener.onLoadMore()
                        loading = true
                    }
                }
            })
        }
    }


    fun setList(repos: List<Repo>) {
        repoList.clear()
        repoList.addAll(repos)
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
        }

        fun bind(repo: Repo) {
            binding.myFlipView.setFlipped(false)
            binding.tvRepoName.text = repo.name
            binding.tvRepoName.isSelected = true
            binding.tvWatch.text = Helper.numberFormatter(repo.watchersCount)
            binding.tvDateUpdated.text = Helper.dateFormatterAlt(repo.updatedAt)
            binding.tvFork.text = Helper.numberFormatter(repo.forksCount)
            binding.tvLanguage.text = repo.language

            binding.root.findViewById<ImageView>(R.id.ivRepoAuthor)
                .setOnClickListener(View.OnClickListener {
                    onItemClickListener.onItemClick(
                        repoList[bindingAdapterPosition],
                        binding.root.findViewById<ImageView>(R.id.ivRepoAuthor)
                    )
                })
            binding.root.findViewById<TextView>(R.id.tvRepoName)
                .setOnClickListener(View.OnClickListener {
                    onItemClickListener.onItemClick(
                        repoList[bindingAdapterPosition],
                        binding.root.findViewById<ImageView>(R.id.tvRepoName)
                    )
                })
            binding.ivRepoAuthor.visibility = View.GONE
            binding.tvAuthorName.visibility = View.GONE
        }

        override fun onClick(v: View?) {

        }
    }
}


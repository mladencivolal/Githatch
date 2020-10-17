package com.example.githatch.presentation.owner

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
        }

        fun bind(repo: Repo) {
            binding.apply {
                myFlipView.setFlipped(false)
                tvRepoName.text = repo.name
                tvRepoName.isSelected = true
                tvWatch.text = Helper.numberFormatter(repo.watchersCount)
                tvDateUpdated.text = Helper.dateFormatterAlt(repo.updatedAt)
                tvFork.text = Helper.numberFormatter(repo.forksCount)
                tvLanguage.text = repo.language
                root.findViewById<ImageView>(R.id.ivRepoAuthor)
                    .setOnClickListener {
                        onItemClickListener.onItemClick(
                            repoList[bindingAdapterPosition],
                            binding.root.findViewById<ImageView>(R.id.ivRepoAuthor)
                        )
                    }
                root.findViewById<TextView>(R.id.tvRepoName)
                    .setOnClickListener {
                        onItemClickListener.onItemClick(
                            repoList[bindingAdapterPosition],
                            binding.root.findViewById<ImageView>(R.id.tvRepoName)
                        )
                    }
                myFlipView.setOnClickListener {
                    binding.myFlipView.flip()
                    CoroutineScope(Dispatchers.Main).launch {
                        if (binding.myFlipView.isFlipped()) {
                            delay(300)
                            binding.apply {
                                tvDescription.text =
                                    repoList[bindingAdapterPosition].description.toString()
                                ivRepoAuthor.isClickable = false
                                tvRepoName.isClickable = false
                            }
                        } else {
                            delay(300)
                            binding.apply {
                                ivRepoAuthor.isClickable = true
                                tvRepoName.isClickable = true
                                tvDescription.text = ""
                            }
                        }
                    }
                }
                ivRepoAuthor.visibility = View.GONE
                tvAuthorName.visibility = View.GONE
            }
        }
        override fun onClick(v: View?) {}
    }
}

